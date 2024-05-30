package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.ShiftRepository;
import bgu.adss.fff.dev.data.ShiftRoleRequirementRepository;
import bgu.adss.fff.dev.domain.models.*;
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
                List.of(new Role("Certified Bazzinga", false)),
                new EmploymentTerms(LocalDate.now(), JobType.FULL_TIME,
                        null, .0f, .0f, 1),
                10, 100, 100100, branch
        );
    }

    @Test
    void testGetShift() {
        EmbeddedShiftId id = new EmbeddedShiftId(LocalDate.now(), ShiftDayPart.MORNING, branch);
        when(repository.findById(id)).thenReturn(Optional.empty());

        Shift response = service.getShift(LocalDate.now(), ShiftDayPart.MORNING, branch);
        assertEquals(id, response.getId());
        assertFalse(response.isLocked());
        assertEquals(branch.getName(), response.getBranchName());
        assertTrue(response.getAssignedEmployees().isEmpty());
        assertTrue(response.getAvailableEmployees().isEmpty());
        assertTrue(response.getRequiredRoles().isEmpty());
    }

    @Test
    void testReoccurringRoles() {
        EmbeddedShiftId id = new EmbeddedShiftId(LocalDate.now(), ShiftDayPart.MORNING, branch);
        ShiftRoleRequirement requirement = new ShiftRoleRequirement(
                LocalDate.now().getDayOfWeek(), ShiftDayPart.MORNING, new Role(
                        "Crabby Patty Critique", false), branch);

        when(reqRoleRepository.save(requirement)).thenReturn(requirement);
        when(reqRoleRepository.findByIdWeekDayAndIdPartAndIdBranchName(
                    LocalDate.now().getDayOfWeek(), ShiftDayPart.MORNING, branch.getName()))
                .thenReturn(Arrays.asList(requirement)); // do not use List.of(T) since its unmodifiable.
        when(repository.findById(id)).thenReturn(Optional.empty());
        when(repository.getRangeOfShiftsByBranch(
                 branch, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)))
                .thenReturn(Arrays.asList(
                        new Shift(LocalDate.now(), ShiftDayPart.MORNING, false, branch
                )));


        Shift response = service.getShift(LocalDate.now(), ShiftDayPart.MORNING, branch);
        assertTrue(response.getRequiredRoles().contains(requirement.getRole()));

        assertTrue(service.getShifts(
                    LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))
                .stream()
                .allMatch(
                        (s) -> s.getRequiredRoles().contains(requirement.getRole())
                )
        );
    }

    @Test
    void testReportingAvailability() {
        EmbeddedShiftId id = new EmbeddedShiftId(LocalDate.now(), ShiftDayPart.MORNING, branch);
        Shift shift = new Shift(LocalDate.now(), ShiftDayPart.MORNING, branch);

        when(employeeService.getEmployee(0L)).thenReturn(sheldon);
        when(repository.findById(id)).thenReturn(Optional.of(shift));
        when(repository.save(shift)).thenReturn(shift);

        service.reportAvailability(0L, LocalDate.now(), ShiftDayPart.MORNING);
        assertTrue(shift.getAvailableEmployees().contains(sheldon));
        assertTrue(service.getAvailableEmployees(LocalDate.now(), ShiftDayPart.MORNING, branch).contains(sheldon));
        service.reportAvailability(0L, LocalDate.now(), ShiftDayPart.MORNING);
        assertFalse(shift.getAvailableEmployees().contains(sheldon));
        assertFalse(service.getAvailableEmployees(LocalDate.now(), ShiftDayPart.MORNING, branch).contains(sheldon));
    }

    @Test
    void testAssignEmployees() {
        EmbeddedShiftId id = new EmbeddedShiftId(LocalDate.now(), ShiftDayPart.MORNING, branch);
        Shift shift = new Shift(LocalDate.now(), ShiftDayPart.MORNING, branch);

        when(employeeService.getEmployee(0L)).thenReturn(sheldon);
        when(repository.findById(id)).thenReturn(Optional.of(shift));
        when(repository.save(shift)).thenReturn(shift);

        service.assignEmployees(List.of(sheldon), LocalDate.now(), ShiftDayPart.MORNING, branch);
        assertTrue(shift.getAssignedEmployees().contains(sheldon));
        assertTrue(service.getAssignedEmployees(LocalDate.now(), ShiftDayPart.MORNING, branch).contains(sheldon));
    }

    @Test
    void testRequiredRolesReoccurringAndNon() {
        EmbeddedShiftId id = new EmbeddedShiftId(LocalDate.now(), ShiftDayPart.MORNING, branch);

        ShiftRoleRequirement requirement = new ShiftRoleRequirement(
                LocalDate.now().getDayOfWeek(), ShiftDayPart.MORNING, new Role(
                "Certified Procrastinator", false), branch);

        when(reqRoleRepository.save(requirement)).thenReturn(requirement);

        boolean[] removed = {false};
        when(reqRoleRepository.findByIdWeekDayAndIdPartAndIdBranchName(
                LocalDate.now().getDayOfWeek(), ShiftDayPart.MORNING, branch.getName()))
                .thenAnswer(invocationOnMock -> removed[0] ? new ArrayList<>() : Arrays.asList(requirement));
        doAnswer(invocationOnMock -> removed[0] = true).when(reqRoleRepository).delete(requirement);

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(repository.getRangeOfShiftsByBranch(
                branch, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)))
                .thenReturn(Arrays.asList(
                        new Shift(LocalDate.now(), ShiftDayPart.MORNING, false, branch)
                ));
        when(roleService.getRole("Certified Procrastinator")).thenReturn(requirement.getRole());

        service.addRequiredRole("Certified Procrastinator", LocalDate.now(), ShiftDayPart.MORNING, true, branch);
        assertTrue(service.getShift(LocalDate.now(), ShiftDayPart.MORNING, branch)
                .getRequiredRoles().contains(requirement.getRole()));

        service.remRequiredRole("Certified Procrastinator", LocalDate.now(), ShiftDayPart.MORNING, false, branch);
        assertFalse(service.getShift(LocalDate.now(), ShiftDayPart.MORNING, branch)
                .getRequiredRoles().contains(requirement.getRole()));
    }
}
