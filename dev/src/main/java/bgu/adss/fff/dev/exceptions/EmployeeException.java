package bgu.adss.fff.dev.exceptions;

public class EmployeeException extends RuntimeException {
    private EmployeeException(String msg) {
        super(msg);
    }

    public static EmployeeException alreadyExists(long id) {
        return new EmployeeException("Employee with id " + id + " already exists.");
    }

    public static EmployeeException notFound(long id) {
        return new EmployeeException("Employee with id " + id + " was not found.");
    }

    public static EmployeeException illegalField(long id, String resource, String details) {
        return new EmployeeException(String.format("""
                At %d, bad/illegal request:
                Resource: %s
                Details:
                %s
                """, id, resource, details));
    }
}
