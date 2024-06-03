package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.ShiftRoleRequirementRepository;
import bgu.adss.fff.dev.data.SystemConfiguration;
import bgu.adss.fff.dev.domain.models.*;
import bgu.adss.fff.dev.data.ShiftRepository;
import bgu.adss.fff.dev.exceptions.ShiftException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static bgu.adss.fff.dev.domain.models.Constants.DAY_HOURS;

@Service
public class ShiftServiceImpl implements ShiftService {
    private final ShiftRepository repository;
    private final ShiftRoleRequirementRepository reqRoleRepository;

    private final RoleService roleService;
    private final EmployeeService employeeService;
    private final BranchService branchService;

    /**
     * For optimization purposes.
     */
    private Integer cutoff;

    @Autowired
    public ShiftServiceImpl(ShiftRepository repository,
                            ShiftRoleRequirementRepository reqRoleRepository,
                            RoleService roleService,
                            EmployeeService employeeService,
                            BranchService branchService) {
        this.repository = repository;
        this.reqRoleRepository = reqRoleRepository;
        this.roleService = roleService;
        this.employeeService = employeeService;
        this.branchService = branchService;

        this.cutoff = null;
    }

    /**
     * Checks for any save d reoccurring role constraints for this day & day part & branch and applies them to the shift
     * if not present already.
     *
     * @param shift The shift.
     * @return the same instance/reference that was provided.
     */
    private Shift applyRecurringRoles(Shift shift) {
        // If the shift is already locked, then no need to change it.
        if(shift.isLocked()) return shift;
        // Retrieves all the role requirements by shift and day, then for each one of them it takes the role:
        List<Role> roles = reqRoleRepository.findByIdWeekDayAndIdPartAndIdBranchName(
                shift.getDate().getDayOfWeek(), shift.getShiftDayPart(), shift.getBranchName()).stream()
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
     * @param currentState if the current shift is already locked.
     * @return true if and only if the shift <b>is locked.</b>
     */
    private boolean lockHelper(LocalDate date, ShiftState currentState) {
        if(cutoff == null) {
            cutoff = new SystemConfiguration().getCutoffTime();
        }
        /* Explanation if you're banging your head to understand it:
         * state == f_lock :=> true
         * state == f_unlock :=> false
         * state != f_lock | f_unlock :=> shift.date - (cutoff + 1) < today.date
         */
        return currentState == ShiftState.FORCE_LOCK || (
                !(currentState == ShiftState.FORCE_UNLOCK)
                &&
                date.minusDays(cutoff / DAY_HOURS + 1).isBefore(LocalDate.now())
        );
    }

    /**
     * Returns the shift if present in the database or creates a new one.
     *
     * @param date the date associated with the shift.
     * @param dayPart the day part of the shift.
     * @param branch the branch of the shift.
     * @return the requested shift.
     */
    private Shift getShiftOrClean(LocalDate date, ShiftDayPart dayPart, Branch branch) {
        Shift out = repository.findById(new EmbeddedShiftId(date, dayPart, branchService.getBranch(branch.getName())))
                .orElse(new Shift(date, dayPart, false, branchService.getBranch(branch.getName())));

        // If one of those is assigned we cannot change this state.
        if(out.getLockState() != ShiftState.FORCE_LOCK || out.getLockState() != ShiftState.FORCE_UNLOCK) {
            out.setLocked(lockHelper(date, out.getLockState()));
        }
        return out;
    }

    @Override
    public Shift getShift(LocalDate date, ShiftDayPart dayPart, Branch branch) {
        Shift shift = getShiftOrClean(date, dayPart, branch);
        return applyRecurringRoles(shift);
    }

    @Override
    public List<Shift> getShifts(LocalDate from, LocalDate to) {
        List<Shift> shifts = repository.getRangeOfShifts(from, to);
        for(Shift s: shifts) {
            s.setLocked(lockHelper(s.getDate(), s.getLockState()));
            applyRecurringRoles(s);
        }
        return shifts;
    }

    @Override
    public List<Shift> getShiftsByBranch(LocalDate from, LocalDate to, Branch branch) {
        List<Shift> shifts = repository.getRangeOfShiftsByBranch(branch, from, to);
        for(Shift s: shifts) {
            s.setLocked(lockHelper(s.getDate(), s.getLockState()));
            applyRecurringRoles(s);
        }
        return shifts;
    }

    @Override
    public void lockShift(LocalDate date, ShiftDayPart dayPart, Branch branch) {
        Shift shift = getShiftOrClean(date, dayPart, branch);

        boolean hasShiftManger = false;
        for(Employee emp : shift.getAssignedEmployees()) {
            hasShiftManger |= isShiftMangerHelper(emp);
        }
        if(!hasShiftManger) {
            throw ShiftException.noShiftManger(date, dayPart);
        }

        shift.setLocked(ShiftState.FORCE_LOCK);
        repository.save(shift);
    }

    @Override
    public void unlockShift(LocalDate date, ShiftDayPart dayPart, Branch branch) {
        Shift shift = getShiftOrClean(date, dayPart, branch);
        shift.setLocked(ShiftState.FORCE_UNLOCK);
        repository.save(shift);
    }

    @Override
    public void reportAvailability(Long empId, LocalDate date, ShiftDayPart dayPart) {
        Employee emp = employeeService.getEmployee(empId);
        Shift shift = getShiftOrClean(date, dayPart, emp.getBranch());
        if(shift.isLocked()) {
            throw ShiftException.locked(date);
        }
        shift.addOrRemoveAvailableEmployee(emp);
        repository.save(shift);
    }

    @Override
    public List<Employee> getAvailableEmployees(LocalDate date, ShiftDayPart dayPart, Branch branch) {
        return getShiftOrClean(date, dayPart, branch).getAvailableEmployees();
    }

    @Override
    public List<Employee> getAssignedEmployees(LocalDate date, ShiftDayPart dayPart, Branch branch) {
        return getShiftOrClean(date, dayPart, branch).getAssignedEmployees();
    }

    /**
     * Assigns a list of employees to a specified shift, overwriting any previous assignment.
     * @param employees The employees to assign.
     * @param date The date of the shift.
     * @param dayPart The day part of the shift.
     * @param branch The branch of the shift.
     * @throws NullPointerException if the employees list is null.
     * @throws ShiftException if at least one of the employees are not assigned to the specified branch.
     */
    @Override
    public void assignEmployees(List<Employee> employees, LocalDate date, ShiftDayPart dayPart, Branch branch) {
        if(employees == null) {
            throw new NullPointerException("Cannot assign null.");
        }

        Shift shift = applyRecurringRoles(getShiftOrClean(date, dayPart, branch));

        // Loads all the employees, and collects their roles:
        final List<Employee> emps = new ArrayList<>();
        boolean hasShiftManger = false;
        for(Employee emp : employees) {
            emp = employeeService.getEmployee(emp.getId());
            if (!emp.getBranch().getName().equals(shift.getBranchName())) {
                throw ShiftException.illegalAssignment(shift.getBranchName(), emp.getName());
            }
            emps.add(emp);
            hasShiftManger |= isShiftMangerHelper(emp);
        }
        if(!hasShiftManger) {
            throw ShiftException.noShiftManger(date, dayPart);
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
        return false;
    }

    @Override
    public void addRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean reoccurring, Branch branch) {
        Role roleInstance = roleService.getRole(role);
        if(reoccurring) {
            reqRoleRepository.save(new ShiftRoleRequirement(
                    date.getDayOfWeek(), dayPart, roleInstance, branchService.getBranch(branch.getName())));
        } else {
            Shift shift = getShiftOrClean(date, dayPart, branch);
            shift.addRequiredRole(roleInstance);
            repository.save(shift);
        }
    }

    @Override
    public void remRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean once, Branch branch) {
        Role roleInstance = roleService.getRole(role);
        if(once) {
            Shift shift = getShiftOrClean(date, dayPart, branch);
            shift.removeRequiredRole(roleInstance);
            repository.save(shift);
        } else {
            List<ShiftRoleRequirement> reqs = reqRoleRepository.findByIdWeekDayAndIdPartAndIdBranchName(
                    date.getDayOfWeek(), dayPart, branch.getName());
            for(ShiftRoleRequirement req : reqs) {
                if(req.getRole().getName().equals(role)) {
                    reqRoleRepository.delete(req);
                    return;
                }
            }
        }
    }
}
