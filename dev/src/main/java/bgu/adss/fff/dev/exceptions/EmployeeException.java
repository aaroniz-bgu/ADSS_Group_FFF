package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;

public class EmployeeException extends AppException {
    private EmployeeException(String msg, HttpStatus status) {
        super(msg, status);
    }

    public static EmployeeException alreadyExists(long id) {
        return new EmployeeException("Employee with id " + id + " already exists.", HttpStatus.CONFLICT);
    }

    public static EmployeeException notFound(long id) {
        return new EmployeeException("Employee with id " + id + " was not found.", HttpStatus.NOT_FOUND);
    }

    public static EmployeeException illegalField(long id, String resource, String details) {
        return new EmployeeException(String.format("""
                At %d, bad/illegal request:
                Resource: %s
                Details:
                %s
                """, id, resource, details), HttpStatus.BAD_REQUEST);
    }
}
