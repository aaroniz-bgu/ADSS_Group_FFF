package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.exceptions.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDetails> appExceptionHandler(AppException ex, WebRequest request) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorDetails(ex.getStatus().value(), ex.getMessage(), "No further information"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> unexpectedExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.internalServerError().body(new ErrorDetails(500,
                "An unexpected error occurred. Please try again later.",
                "If the problem persists, contact support."));
    }
}
