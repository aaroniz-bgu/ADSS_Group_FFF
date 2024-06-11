package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;

public class ProductException extends AppException {

    /**
     * ProductException constructor
     * @param message exception message
     * @param status exception status
     */
    public ProductException(String message, HttpStatus status) {
        super(message, status);
    }
}
