package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.ShiftRoleRequirementRepository;
import bgu.adss.fff.dev.domain.models.*;
import bgu.adss.fff.dev.data.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftServiceImpl implements ShiftService {
    private final ShiftRepository repository;
    private final ShiftRoleRequirementRepository reqRoleRepository;

    private final RoleService roleService;
    private final EmployeeService employeeService;

    @Autowired
    public ShiftServiceImpl(ShiftRepository repository,
                            ShiftRoleRequirementRepository reqRoleRepository,
                            RoleService roleService,
                            EmployeeService employeeService) {
        this.repository = repository;
        this.reqRoleRepository = reqRoleRepository;
        this.roleService = roleService;
        this.employeeService = employeeService;
    }

    /**
     * Checks for any save d reoccurring role constraints for this day & day part and applies them to the shift
     * if not present already.
     *
     * @param shift The shift.
     * @return the same instance/reference that was provided.
     */
    private Shift applyRecurringRoles(Shift shift) {
        // If the shift is already locked, then no need to change it.
        if(shift.isLocked()) return shift;
        // Retrieves all the role requirements by shift and day, then for each one of them it takes the role:
        List<Role> roles = reqRoleRepository.findByIdWeekDayAndIdPart(
                shift.getDate().getDayOfWeek(), shift.getShiftDayPart()).stream()
                .map(ShiftRoleRequirement::getRole)
                .toList();
        // get the existing required roles:
        List<Role> existingRoles = shift.getRequiredRoles();
        for(Role r : roles) {
            if(!existingRoles.contains(r)) {
                existingRoles.add(r);
            }
        }
        return shift;
    }

    /**
     * Checks whether a shift with a given date should be already locked.
     *
     * @param date the date which the shift occur at.
     * @return true if and only if the shift <b>can stay unlocked.</b>
     */
    private boolean lockHelper(LocalDate date) {
        // TODO IN THE FUTURE CHECK IF TIME PASSED...
        return date.isBefore(LocalDate.now() /* || ((allRolesAssigned(...)) && (barrierPassed()))*/);
    }

    /**
     * Returns the shift if present in the database or creates a new one.
     *
     * @param date the date associated with the shift.
     * @param dayPart the day part of the shift.
     * @return the requested shift.
     */
    private Shift getShiftOrClean(LocalDate date, ShiftDayPart dayPart) {
        return repository.findById(new EmbeddedShiftId(date, dayPart))
                .orElse(new Shift(date, dayPart, lockHelper(date)));
    }

    @Override
    public Shift getShift(LocalDate date, ShiftDayPart dayPart) {
        Shift shift = getShiftOrClean(date, dayPart);
        return applyRecurringRoles(shift);
    }

    @Override
    public List<Shift> getShifts(LocalDate from, LocalDate to) {
        List<Shift> shifts = repository.getRangeOfShiftsByBranch(from, to);
        for(Shift s: shifts) {
            s.setLocked(lockHelper(s.getDate()));
            applyRecurringRoles(s);
        }
        return shifts;
    }

    // IDK whether it's good that it is duplicated but just to be safe atm:
    @Override
    public void lockShift(LocalDate date, ShiftDayPart dayPart) {
        Shift shift = getShiftOrClean(date, dayPart);

        boolean hasShiftManger = false;
        for(Employee emp : shift.getAssignedEmployees()) {
            hasShiftManger |= isShiftMangerHelper(emp);
        }
        if(!hasShiftManger) {
            // TODO Throw...
        }

        shift.setLocked(true);
        repository.save(shift);
    }

    @Override
    public void unlockShift(LocalDate date, ShiftDayPart dayPart) {
        Shift shift = getShiftOrClean(date, dayPart);
        shift.setLocked(false);
        repository.save(shift);
    }

    @Override
    public void reportAvailability(Long empId, LocalDate date, ShiftDayPart dayPart) {
        Employee emp = employeeService.getEmployee(empId);
        Shift shift = getShiftOrClean(date, dayPart);
        shift.addOrRemoveAvailableEmployee(emp);
        repository.save(shift);
    }

    @Override
    public List<Employee> getAvailableEmployees(LocalDate date, ShiftDayPart dayPart) {
        return getShiftOrClean(date, dayPart).getAvailableEmployees();
    }

    @Override
    public List<Employee> getAssignedEmployees(LocalDate date, ShiftDayPart dayPart) {
        return getShiftOrClean(date, dayPart).getAssignedEmployees();
    }

    @Override
    public void assignEmployees(List<Employee> employees, LocalDate date, ShiftDayPart dayPart) {
        if(employees == null) {
            throw new NullPointerException("Cannot assign null.");
        }

        Shift shift = applyRecurringRoles(getShiftOrClean(date, dayPart));

        // Loads all the employees, and collects their roles:
        final List<Employee> emps = new ArrayList<>();
        boolean hasShiftManger = false;
        for(Employee emp : employees) {
            emp = employeeService.getEmployee(emp.getId());
            emps.add(emp);
            hasShiftManger |= isShiftMangerHelper(emp);
        }
        if(!hasShiftManger) {
            // TODO Throw...
        }

        // TODO Check if all roles are fulfilled...

        shift.setAssignedEmployees(emps);
        repository.save(shift);
    }

    private boolean isShiftMangerHelper(Employee emp) {
        for(Role r: emp.getRoles()) {
            if(r.isShiftManager()) return true;
        }
        return false;
    }

    private boolean checkRolesFulfilled(Shift shift, List<Employee> employees) {
        return false; // TODO.
    }

    @Override
    public void addRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean reoccurring) {
        Role roleInstance = roleService.getRole(role);
        if(reoccurring) {
            reqRoleRepository.save(new ShiftRoleRequirement(date.getDayOfWeek(), dayPart, roleInstance));
        } else {
            Shift shift = getShiftOrClean(date, dayPart);
            shift.addRequiredRole(roleInstance);
            repository.save(shift);
        }
    }

    @Override
    public void remRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean once) {
        Role roleInstance = roleService.getRole(role);
        if(once) {
            Shift shift = getShiftOrClean(date, dayPart);
            shift.removeRequiredRole(roleInstance);
            repository.save(shift);
        } else {
            List<ShiftRoleRequirement> reqs = reqRoleRepository.findByIdWeekDayAndIdPart(
                    date.getDayOfWeek(), dayPart);
            for(ShiftRoleRequirement req : reqs) {
                if(req.getRole().getName().equals(role)) {
                    reqRoleRepository.delete(req);
                    return;
                }
            }
        }
    }
}
