package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Delivery;

import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryService {
    Delivery registerDelivery(String source, LocalDateTime start, long truckNumber, String license, List<String> destinations);
}
