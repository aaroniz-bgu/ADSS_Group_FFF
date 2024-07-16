package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.ShiftRepository;
import bgu.adss.fff.dev.data.ShiftRoleRequirementRepository;
import bgu.adss.fff.dev.domain.models.*;
import bgu.adss.fff.dev.exceptions.ShiftException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ShiftServiceImplTests {
    @MockBean
    private ShiftRepository repository;
    @MockBean
    private ShiftRoleRequirementRepository reqRoleRepository;
    @MockBean
    private RoleService roleService;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private BranchService branchService;

    @Autowired
    private ShiftService service;

    private Branch branch;
    private Employee sheldon;

    @BeforeEach
    void before() {
        branch = new Branch("Middle Earth");
        sheldon = new Employee(
                0L,
                "Sheldon Copper",
                List.of(new Role("Certified Bazzinga", true, true)),
                new EmploymentTerms(LocalDate.now(), JobType.FULL_TIME,
                        null, .0f, .0f, 1),
                10, 100, 100100, branch
        );
        when(branchService.getBranch(branch.getName())).thenReturn(branch);
    }

    @Test
    void testGetShift() {
        EmbeddedShiftId id = new EmbeddedShiftId(LocalDate.now().plusDays(2), ShiftDayPart.MORNING, branch);
        when(repository.findById(id)).thenReturn(Optional.empty());

        Shift response = service.getShift(LocalDate.now().plusDays(2), ShiftDayPart.MORNING, branch);
        assertEquals(id, response.getId());
        assertFalse(response.isLocked());
        assertEquals(branch.getName(), response.getBranchName());
        assertTrue(response.getAssignedEmployees().isEmpty());
        assertTrue(response.getAvailableEmployees().isEmpty());
        assertTrue(response.getRequiredRoles().isEmpty());
    }

    @Test
    void testReoccurringRoles() {
        LocalDate date = LocalDate.now().plusDays(2);
        EmbeddedShiftId id = new EmbeddedShiftId(date, ShiftDayPart.MORNING, branch);
        ShiftRoleRequirement requirement = new ShiftRoleRequirement(
                date.getDayOfWeek(), ShiftDayPart.MORNING, new Role(
                        "Crabby Patty Critique", false, false), branch);

        when(reqRoleRepository.save(requirement)).thenReturn(requirement);
        when(reqRoleRepository.findByIdWeekDayAndIdPartAndIdBranchName(
                date.getDayOfWeek(), ShiftDayPart.MORNING, branch.getName()))
                .thenReturn(Arrays.asList(requirement)); // do not use List.of(T) since its unmodifiable.
        when(repository.findById(id)).thenReturn(Optional.empty());
        when(repository.getRangeOfShiftsByBranch(
                 branch, date.minusDays(1), date.plusDays(1)))
                .thenReturn(Arrays.asList(
                        new Shift(date, ShiftDayPart.MORNING, false, branch
                )));


        Shift response = service.getShift(date, ShiftDayPart.MORNING, branch);
        assertTrue(response.getRequiredRoles().contains(requirement.getRole()));

        assertTrue(service.getShifts(
                        date.minusDays(1), date.plusDays(1))
                .stream()
                .allMatch(
                        (s) -> s.getRequiredRoles().contains(requirement.getRole())
                )
        );
    }

    @Test
    void testReportingAvailability() {
        LocalDate date = LocalDate.now().plusDays(2);
        EmbeddedShiftId id = new EmbeddedShiftId(date, ShiftDayPart.MORNING, branch);
        Shift shift = new Shift(date, ShiftDayPart.MORNING, branch);

        when(employeeService.getEmployee(0L)).thenReturn(sheldon);
        when(repository.findById(id)).thenReturn(Optional.of(shift));
        when(repository.save(shift)).thenReturn(shift);

        service.reportAvailability(0L, date, ShiftDayPart.MORNING);
        assertTrue(shift.getAvailableEmployees().contains(sheldon));
        assertTrue(service.getAvailableEmployees(date, ShiftDayPart.MORNING, branch).contains(sheldon));
        service.reportAvailability(0L, date, ShiftDayPart.MORNING);
        assertFalse(shift.getAvailableEmployees().contains(sheldon));
        assertFalse(service.getAvailableEmployees(date, ShiftDayPart.MORNING, branch).contains(sheldon));
    }

    @Test
    void testAssignEmployees() {
        LocalDate date = LocalDate.now().plusDays(2);
        EmbeddedShiftId id = new EmbeddedShiftId(date, ShiftDayPart.MORNING, branch);
        Shift shift = new Shift(date, ShiftDayPart.MORNING, branch);

        when(employeeService.getEmployee(0L)).thenReturn(sheldon);
        when(repository.findById(id)).thenReturn(Optional.of(shift));
        when(repository.save(shift)).thenReturn(shift);
        service.reportAvailability(0L, date, ShiftDayPart.MORNING);

        service.assignEmployees(List.of(sheldon), date, ShiftDayPart.MORNING, branch);
        assertTrue(shift.getAssignedEmployees().contains(sheldon));
        assertTrue(service.getAssignedEmployees(date, ShiftDayPart.MORNING, branch).contains(sheldon));
    }

    @Test
    void testRequiredRolesReoccurringAndNon() {
        LocalDate date = LocalDate.now().plusDays(2);

        EmbeddedShiftId id = new EmbeddedShiftId(date, ShiftDayPart.MORNING, branch);

        ShiftRoleRequirement requirement = new ShiftRoleRequirement(
                date.getDayOfWeek(), ShiftDayPart.MORNING, new Role(
                "Certified Procrastinator", false, false), branch);

        when(reqRoleRepository.save(requirement)).thenReturn(requirement);

        boolean[] removed = {false};
        when(reqRoleRepository.findByIdWeekDayAndIdPartAndIdBranchName(
                date.getDayOfWeek(), ShiftDayPart.MORNING, branch.getName()))
                .thenAnswer(invocationOnMock -> removed[0] ? new ArrayList<>() : Arrays.asList(requirement));
        doAnswer(invocationOnMock -> removed[0] = true).when(reqRoleRepository).delete(requirement);

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(repository.getRangeOfShiftsByBranch(
                branch, date.minusDays(1), date.plusDays(1)))
                .thenReturn(Arrays.asList(
                        new Shift(date, ShiftDayPart.MORNING, false, branch)
                ));
        when(roleService.getRole("Certified Procrastinator")).thenReturn(requirement.getRole());

        service.addRequiredRole("Certified Procrastinator", date, ShiftDayPart.MORNING, true, branch);
        assertTrue(service.getShift(date, ShiftDayPart.MORNING, branch)
                .getRequiredRoles().contains(requirement.getRole()));

        service.remRequiredRole("Certified Procrastinator", date, ShiftDayPart.MORNING, false, branch);
        assertFalse(service.getShift(date, ShiftDayPart.MORNING, branch)
                .getRequiredRoles().contains(requirement.getRole()));
    }

    @Test
    void testNotAvailableEmployees() {
        LocalDate date = LocalDate.now().plusDays(2);
        EmbeddedShiftId id = new EmbeddedShiftId(date, ShiftDayPart.MORNING, branch);
        Shift shift = new Shift(date, ShiftDayPart.MORNING, branch);

        when(employeeService.getEmployee(0L)).thenReturn(sheldon);
        when(repository.findById(id)).thenReturn(Optional.of(shift));
        when(repository.save(shift)).thenReturn(shift);
        assertThrows(ShiftException.class, () -> service.assignEmployees(List.of(sheldon), date, ShiftDayPart.MORNING, branch));
        service.reportAvailability(0L, date, ShiftDayPart.MORNING);
        assertDoesNotThrow(() -> service.assignEmployees(List.of(sheldon), date, ShiftDayPart.MORNING, branch));
    }
}
