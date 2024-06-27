package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.DeliveryRepository;
import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Delivery;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.domain.models.RoleField;
import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static bgu.adss.fff.dev.domain.models.Constants.SHIFT_CHANGE_HOUR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DeliveryServiceImplTests {

    @Autowired
    private DeliveryService service;

    @MockBean
    private ShiftService shiftService;
    @MockBean
    private BranchService branchService;
    @MockBean
    private DeliveryRepository repository;
    @MockBean
    private EmployeeService employeeService;

    private Branch b1;
    private Branch b2;
    private Employee driver;
    private Employee storekeeper;
    private Role drivingRole;
    private Role storekeeperRole;

    private Set<Delivery> repoMock;

    @BeforeEach
    void beforeAll() {
        b1 = new Branch("b1");
        b2 = new Branch("b2");

        drivingRole = new Role("driver", false, false);
        storekeeperRole = new Role("storekeeper", false, false);

        driver = new Employee(1L, "Driver Driver",
                List.of(drivingRole), null, 0, 0, 0, b1);
        storekeeper = new Employee(2L, "Store Keeper",
                List.of(storekeeperRole), null, 0, 0, 0, b2);

        repoMock = new LinkedHashSet<>();
    }

    @Test
    void testRegisterDelivery() {
        // params:
        LocalDateTime time = LocalDateTime.now();
        ShiftDayPart part = time.getHour() < SHIFT_CHANGE_HOUR ? ShiftDayPart.MORNING : ShiftDayPart.EVENING;
        final String license = "c1";
        final long truck = 1234567L;

        // mock branchService:
        when(branchService.getBranch("b1")).thenReturn(b1);
        when(branchService.getBranch("b2")).thenReturn(b2);

        // mock shiftService:
        when(shiftService.getAvailableEmployees(time.toLocalDate(), part, b1)).thenReturn(List.of(driver));
        when(shiftService.getAvailableEmployees(time.toLocalDate(), part, b2)).thenReturn(List.of(storekeeper));

        // mock employeeService:
        when(employeeService.getCustomField(driver.getId(), drivingRole.getName(), "license")).thenReturn(
                new RoleField(driver, drivingRole, "license", license)
        );

        // mock repository:
        when(repository.save(any(Delivery.class))).then(i -> {
            Delivery delivery = i.getArgument(0);
            repoMock.add(delivery);
            return delivery;
        });

        Delivery delivery = service.registerDelivery(b1.getName(), time, truck, license, List.of(b2.getName()));

        assertEquals(driver.getId(), delivery.getDriver().getId());
    }

    @Test
    void testGetAll() {
        testRegisterDelivery();
        when(repository.findAll()).thenReturn(repoMock.stream().toList());

        assertEquals(repoMock.stream().toList(), service.getDeliveries());
    }
}
