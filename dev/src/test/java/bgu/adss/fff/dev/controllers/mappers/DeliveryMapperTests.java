package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.DeliveryDto;
import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Delivery;
import bgu.adss.fff.dev.domain.models.Employee;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeliveryMapperTests {

    @Test
    void testMap() {
        Employee driver = new Employee(0L, "Hugh Jass", null, null, 0, 0 ,0, null);
        LocalDateTime time = LocalDateTime.now();
        Delivery delivery = new Delivery(
                new Branch("main"),
                driver, time, 0L, "C1",
                List.of(new Branch("ben-gurion")));

        DeliveryDto dto = DeliveryMapper.map(delivery);

        assertEquals(delivery.getStartTime(), dto.start());
        assertEquals(delivery.getSource().getName(), dto.source());
        assertEquals(delivery.getDriver().getId(), dto.empId());
    }
}
