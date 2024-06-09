package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;

public class ReportException extends AppException {
    public ReportException(String message, HttpStatus status) {
        super(message, status);
    }
}
