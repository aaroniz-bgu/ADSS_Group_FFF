package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;

public class ReportException extends AppException {

    /**
     * ReportException constructor
     * @param message exception message
     * @param status exception status
     */
    public ReportException(String message, HttpStatus status) {
        super(message, status);
    }
}
