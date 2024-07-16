package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.EmployeeRepository;
import bgu.adss.fff.dev.data.RoleFieldRepository;
import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.EmploymentTerms;
import bgu.adss.fff.dev.domain.models.JobType;
import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.domain.models.RoleField;
import bgu.adss.fff.dev.exceptions.EmployeeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EmployeeServiceImplTests {
    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService service;

    private Employee yonatan;
    private Employee gal;
    @MockBean
    private BranchService branchService;
    @MockBean
    private RoleService roleService;
    @MockBean
    private RoleFieldRepository fieldRepository;

    Branch branch1;
    Branch branch2;

    @BeforeEach
    void before() {
        branch1 = new Branch("Middle Earth");
        branch2 = new Branch("Narnia");
        List<Role> allRoles1 = new ArrayList<>(){{{{
            add(new Role("Jewish Prince", false, false));
            add(new Role("Handsome Ashkenazi", true, false));
        }}}};
        List<Role> allRoles2 = new ArrayList<>(){{{{
            add(new Role("Agent", false, false));
            add(new Role("Ballet Dancer", true, false));
        }}}};
        when(roleService.returnIfExists(allRoles1.stream()
                .map(Role::getName)
                .collect(Collectors.toList()))).thenReturn(allRoles1);
        when(roleService.returnIfExists(allRoles2.stream()
                .map(Role::getName)
                .collect(Collectors.toList()))).thenReturn(allRoles2);
        // Data source: https://www.youtube.com/watch?v=tot02ZOYUmc
        yonatan = new Employee(12345689L, "Yonatan Barak I",
                allRoles1,
                new EmploymentTerms(LocalDate.now(), JobType.CONTRACT, null,
                        80000, -1, 300),
                10, 800, 100100, branch1);
        // Data source: https://eincyclopedia.org/wiki/%D7%92%D7%9C_%D7%92%D7%93%D7%95%D7%AA
        gal = new Employee(420420420L, "Gal G Gadot",
                allRoles2,
                new EmploymentTerms(LocalDate.now(), JobType.FULL_TIME, yonatan,
                        10000, -1, -1),
                10, 420, 420420, branch2
            );
        when(branchService.getBranch(branch1.getName())).thenReturn(branch1);
        when(branchService.getBranch(branch2.getName())).thenReturn(branch2);
    }

    @Test
    void testCreate() {
        when(employeeRepository.saveAndFlush(yonatan)).thenReturn(yonatan);
        assertEquals(yonatan, service.createEmployee(yonatan));
        assertThrows(EmployeeException.class, () -> service.createEmployee(gal));

        when(employeeRepository.findById(yonatan.getId())).thenReturn(Optional.of(yonatan));
        when(employeeRepository.saveAndFlush(gal)).thenReturn(gal);

        gal.getTerms().setHourlyRate(1);
        gal.getTerms().setDaysOff(1);

        assertEquals(gal, service.createEmployee(gal));
    }

    @Test
    void testUpdate() {
        when(employeeRepository.findById(gal.getId())).thenReturn(Optional.of(gal));
        when(employeeRepository.findById(yonatan.getId())).thenReturn(Optional.of(yonatan));
        when(employeeRepository.saveAndFlush(gal)).thenReturn(gal);
        when(employeeRepository.saveAndFlush(yonatan)).thenReturn(yonatan);

        assertEquals(yonatan, service.updateEmployee(yonatan.getId(), yonatan));
        assertEquals(gal, service.updateEmployee(gal.getId(), gal));
        gal.setBranch(branch1);
        assertEquals(yonatan.getBranch(), service.updateEmployee(gal.getId(),gal).getBranch());
    }

    @Test
    void testUpdateTerms() {
        when(employeeRepository.findById(gal.getId())).thenReturn(Optional.of(gal));
        when(employeeRepository.findById(yonatan.getId())).thenReturn(Optional.of(yonatan));

        assertThrows(EmployeeException.class, () -> service.updateEmployementTerms(gal.getId() ,gal.getTerms()));
        gal.getTerms().setHourlyRate(1);
        gal.getTerms().setDaysOff(1);
        assertEquals(gal, service.updateEmployementTerms(gal.getId() ,gal.getTerms()));
    }

    @Test
    void testUpdateCustomField() {
        Role role = new Role("driver", false, false);
        Employee driver = new Employee(159248367L, "Driver Driver",
                List.of(role),
                null, 0,0,0, new Branch("main"));

        String field = "LiCEnSE";
        String value = "C1";

        when(employeeRepository.findById(159248367L)).thenReturn(Optional.of(driver));
        when(roleService.getRole("driver")).thenReturn(role);
        when(fieldRepository.findById(new RoleField.RoleFieldKey(driver, role, field.toLowerCase())))
                .thenReturn(Optional.empty());
        when(fieldRepository.saveAndFlush(any())).then(i -> i.getArguments()[0]);

        RoleField output = service.updateCustomField(driver.getId(), role.getName(), field, value);

        assertEquals(field.toLowerCase(), output.getField());
        assertEquals(value.toLowerCase(), output.getValue());
        assertEquals(driver, output.getEmployee());
        assertEquals(role, output.getRole());
    }

    @Test
    void testGetCustomField() {
        Role role = new Role("driver", false, false);
        Employee driver = new Employee(159248367L, "Driver Driver",
                List.of(role),
                null, 0,0,0, new Branch("main"));

        String field = "LiCEnSE";
        String value = "C1";

        when(employeeRepository.findById(159248367L)).thenReturn(Optional.of(driver));
        when(roleService.getRole("driver")).thenReturn(role);
        when(fieldRepository.findById(new RoleField.RoleFieldKey(driver, role, field.toLowerCase())))
                .thenReturn(Optional.of(new RoleField(driver, role, field.toLowerCase(), value.toLowerCase())));

        RoleField output = service.getCustomField(driver.getId(), role.getName(), field);

        assertEquals(field.toLowerCase(), output.getField());
        assertEquals(value.toLowerCase(), output.getValue());
        assertEquals(driver, output.getEmployee());
        assertEquals(role, output.getRole());
    }
}
