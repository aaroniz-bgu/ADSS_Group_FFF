package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.ShiftRoleRequirementRepository;
import bgu.adss.fff.dev.domain.models.*;
import bgu.adss.fff.dev.exceptions.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class ShiftServicesImpl implements ShiftService {
    private final ShiftRepository repository;
    private final ShiftRoleRequirementRepository reqRoleRepository;

    private final RoleService roleService;
    private final EmployeeService employeeService;

    @Autowired
    public ShiftServicesImpl(ShiftRepository repository,
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
        return !(date.isBefore(LocalDate.now())); // TODO IN THE FUTURE CHECK IF TIME PASSED...
    }

    @Override
    public Shift getShift(LocalDate date, ShiftDayPart dayPart) {
        Shift shift = repository.findById(new EmbeddedShiftId(date, dayPart))
                .orElse(new Shift(date, dayPart, lockHelper(date)));
        return applyRecurringRoles(shift);
    }

    @Override
    public List<Shift> getShifts(LocalDate from, LocalDate to) {
        return null;
    }

    @Override
    public void lockShift(LocalDate date, ShiftDayPart dayPart) {

    }

    @Override
    public void unlockShift(LocalDate date, ShiftDayPart dayPart) {

    }

    @Override
    public void reportAvailability(Long empId, LocalDate date, ShiftDayPart dayPart) {

    }

    @Override
    public List<Employee> getAvailableEmployees(LocalDate date, ShiftDayPart dayPart) {
        return null;
    }

    @Override
    public List<Employee> getAssignedEmployees(LocalDate date, ShiftDayPart dayPart) {
        return null;
    }

    @Override
    public void assignEmployees(List<Employee> employees, LocalDate date, ShiftDayPart dayPart) {

    }

    @Override
    public void addRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean reoccurring) {

    }

    @Override
    public void remRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean once) {

    }
}
