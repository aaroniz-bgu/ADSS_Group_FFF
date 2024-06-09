package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;

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