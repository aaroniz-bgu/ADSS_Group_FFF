package bgu.adss.fff.dev.contracts;

import java.time.LocalDateTime;

public record DeliveryDto(
        long id,
        String source,
        LocalDateTime start,
        long truckNumber,
        String license,
        String[] destinations,
        long empId
) { }
