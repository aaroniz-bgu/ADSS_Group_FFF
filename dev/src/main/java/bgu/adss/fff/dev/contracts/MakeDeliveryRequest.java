package bgu.adss.fff.dev.contracts;

import java.time.LocalDateTime;

public record MakeDeliveryRequest(
        String source,
        LocalDateTime start,
        long truckNumber,
        String license,
        String[] destinations) {
}
