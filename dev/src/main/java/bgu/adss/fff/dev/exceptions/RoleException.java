package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;

public class RoleException extends AppException {
    private RoleException(String msg, HttpStatus status) { super(msg, status); }

    public static RoleException alreadyExists(String name) {
        return new RoleException("Role with name " + name + " already exists.", HttpStatus.CONFLICT);
    }

    public static RoleException notFound(String name) {
        return new RoleException("Role with name " + name + " was not found.", HttpStatus.NOT_FOUND);
    }

    public static RoleException illegalField(String name, String resource, String details) {
        return new RoleException(String.format("""
                At %s, bad/illegal request:
                Resource: %s
                Details:
                %s
                """, name, resource, details), HttpStatus.BAD_REQUEST);
    }
}
