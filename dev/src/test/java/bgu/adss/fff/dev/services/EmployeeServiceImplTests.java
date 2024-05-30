package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.BranchRepository;
import bgu.adss.fff.dev.data.EmployeeRepository;
import bgu.adss.fff.dev.domain.models.*;
import bgu.adss.fff.dev.exceptions.EmployeeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private BranchRepository branchRepository;

    @BeforeEach
    void before() {
        Branch branch1 = new Branch("Middle Earth");
        Branch branch2 = new Branch("Narnia");
        // Data source: https://www.youtube.com/watch?v=tot02ZOYUmc
        yonatan = new Employee(12345689L, "Yonatan Barak I",
                new ArrayList<Role>(){{
                    add(new Role("Jewish Prince", false));
                    add(new Role("Handsome Ashkenazi", true));
                }},
                new EmploymentTerms(LocalDate.now(), JobType.CONTRACT, null,
                        80000, -1, 300),
                10, 800, 100100, branch1);
        // Data source: https://eincyclopedia.org/wiki/%D7%92%D7%9C_%D7%92%D7%93%D7%95%D7%AA
        gal = new Employee(420420420L, "Gal G Gadot",
                new ArrayList<Role>(){{
                    add(new Role("Agent", true));
                    add(new Role("Ballet Dancer", false));
                }},
                new EmploymentTerms(LocalDate.now(), JobType.FULL_TIME, yonatan,
                        10000, -1, -1),
                10, 420, 420420, branch2
            );
        when(branchRepository.findById(branch1.getName())).thenReturn(Optional.of(branch1));
        when(branchRepository.findById(branch2.getName())).thenReturn(Optional.of(branch2));
    }

    @Test
    void testCreate() {
        when(employeeRepository.save(yonatan)).thenReturn(yonatan);
        assertEquals(yonatan, service.createEmployee(yonatan));
        assertThrows(EmployeeException.class, () -> service.createEmployee(gal));

        when(employeeRepository.findById(yonatan.getId())).thenReturn(Optional.of(yonatan));
        when(employeeRepository.save(gal)).thenReturn(gal);

        gal.getTerms().setHourlyRate(1);
        gal.getTerms().setDaysOff(1);

        assertEquals(gal, service.createEmployee(gal));
    }

    @Test
    void testUpdate() {
        when(employeeRepository.findById(gal.getId())).thenReturn(Optional.of(gal));
        when(employeeRepository.findById(yonatan.getId())).thenReturn(Optional.of(yonatan));
        when(employeeRepository.save(gal)).thenReturn(gal);
        when(employeeRepository.save(yonatan)).thenReturn(yonatan);

        assertEquals(yonatan, service.updateEmployee(yonatan.getId(), yonatan));
        assertEquals(gal, service.updateEmployee(gal.getId(), gal));
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
}
