package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;

public class DiscountException extends AppException{
    public DiscountException(String message, HttpStatus status) {
        super(message, status);
    }
}
