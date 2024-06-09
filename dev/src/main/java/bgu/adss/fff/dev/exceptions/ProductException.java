package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;

public class ProductException extends AppException {
    public ProductException(String message, HttpStatus status) {
        super(message, status);
    }
}
