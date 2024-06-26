package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Delivery;

import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryService {
    List<Delivery> getDeliveries();
    Delivery getDelivery(long id);
    Delivery registerDelivery(String source, LocalDateTime start, long truckNumber, String license, List<String> destinations);
}
