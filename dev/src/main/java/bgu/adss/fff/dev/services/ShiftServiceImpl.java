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

        fillHolesHelper(shifts, from, to, branch);
        shifts.sort(this::shiftDatePartComparator);

        return shifts;
    }

    /**
     * Fills the time frame with shifts if they weren't persisted yet.
     * Alters the given list (changes take in place).
     *
     * @param shifts the list of shift to change.
     * @param from   the date from which to start scanning.
     * @param to     the date to which we want to "fill holes" to (included).
     * @param branch the branch which those shifts belong to.
     */
    private void fillHolesHelper(List<Shift> shifts, LocalDate from, LocalDate to, Branch branch) {
        // Sorting before iterating over the shift response:
        shifts.sort(this::shiftDatePartComparator);

        // Adding "holes" which we're not present in the current shift, tried to keep it linear therefore, ugly code.
        LocalDate curr = from;
        ShiftDayPart part = ShiftDayPart.MORNING;

        for(Shift shift : shifts) {
            Shift add;
            while(shiftDatePartComparator((add = new Shift(curr, part, branch)), shift) <= 0) {
                // Since we always flip, we need this to be like this:
                if(shiftDatePartComparator(add, shift) != 0) shifts.add(add);
                // Flip:
                if(part == ShiftDayPart.MORNING) {
                    part = ShiftDayPart.EVENING;
                } else {
                    part = ShiftDayPart.MORNING;
                    curr = curr.plusDays(1);
                }
            }
        }
        // if `to` wasn't reached:
        while(!curr.isAfter(to)) {
            Shift add = new Shift(curr, part, branch);
            shifts.add(add);
            if(part == ShiftDayPart.MORNING) {
                part = ShiftDayPart.EVENING;
            } else {
                part = ShiftDayPart.MORNING;
                curr = curr.plusDays(1);
            }
        }
    }

    /**
     * Returns a negative integer if `a` is before `b`,
     * 0 if they occur in the same time, and positive integer if `a` is later than `b`.
     * <br> firstly, compares dates, if dates are equal then comparing {@link ShiftDayPart} where MORNING < EVENING.
     *
     * @param a first shift.
     * @param b second shift.
     * @return a negative integer if `a` is before `b`,
     * 0 if they occur in the same time, and positive integer if `a` is later than `b`.
     */
    private int shiftDatePartComparator(Shift a, Shift b) {
        if(a.getDate().isBefore(b.getDate())) {
            return -1;
        } else if (a.getDate().isEqual(b.getDate())) {
            return Integer.compare(a.getShiftDayPart().ordinal(), b.getShiftDayPart().ordinal());
        }
        return 1;
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
     * @throws ShiftException if at least one of the employees is not available for the specified shift.
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
            if (!shift.getAvailableEmployees().contains(emp)) {
                throw ShiftException.employeeNotAvailable(emp.getName(), date, dayPart);
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
