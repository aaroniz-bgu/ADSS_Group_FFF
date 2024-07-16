package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.DeliveryDto;
import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Delivery;

public class DeliveryMapper {
    public static DeliveryDto map(Delivery delivery) {
        return new DeliveryDto(
                delivery.getId(),
                delivery.getSource().getName(),
                delivery.getStartTime(),
                delivery.getTruckNumber(),
                delivery.getLicense(),
                delivery.getDestinations().stream()
                        .map(Branch::getName)
                        .toList()
                        .toArray(new String[0]),
                delivery.getDriver().getId()
        );
    }
}
