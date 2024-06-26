package bgu.adss.fff.dev.exceptions;

import bgu.adss.fff.dev.domain.models.Branch;
import org.springframework.http.HttpStatus;

public class DeliveryException extends AppException {
    protected DeliveryException(String message, HttpStatus status) {
        super(message, status);
    }

    public static DeliveryException noAvailableDriver() {
        return new DeliveryException("No available drivers at this date and time.", HttpStatus.NOT_FOUND);
    }

    public static DeliveryException noAvailableStorekeeper(Branch branch) {
        return new DeliveryException("No avaialable storekeeper at the detination: "+branch.getName(),
                HttpStatus.NOT_FOUND);
    }
}
