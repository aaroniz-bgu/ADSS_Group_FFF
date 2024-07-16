package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;

public class DiscountException extends AppException{

    /**
     * DiscountException constructor
     * @param message exception message
     * @param status exception status
     */
    public DiscountException(String message, HttpStatus status) {
        super(message, status);
    }
}
