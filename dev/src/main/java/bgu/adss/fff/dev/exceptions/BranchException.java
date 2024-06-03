package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;

public class BranchException extends AppException {
    private BranchException(String msg, HttpStatus status) { super(msg, status); }

    public static BranchException alreadyExists(String name) {
        return new BranchException("Branch with name " + name + " already exists.", HttpStatus.CONFLICT);
    }

    public static BranchException notFound(String name) {
        return new BranchException("Branch with name " + name + " was not found.", HttpStatus.NOT_FOUND);
    }

    public static BranchException illegalField(String name, String resource, String details) {
        return new BranchException(String.format("""
                At %s, bad/illegal request:
                Resource: %s
                Details:
                %s
                """, name, resource, details), HttpStatus.BAD_REQUEST);
    }
}
