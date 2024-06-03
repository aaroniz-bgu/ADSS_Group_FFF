package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * Used to generalize all custom exceptions that this app uses.
 */
public abstract class AppException extends RuntimeException {

    protected final HttpStatus status;

    protected AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
