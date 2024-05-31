package bgu.adss.fff.dev.exceptions;

public class BranchException extends RuntimeException{
    private BranchException(String msg) { super(msg); }

    public static BranchException alreadyExists(String name) {
        return new BranchException("Branch with name " + name + " already exists.");
    }

    public static BranchException notFound(String name) {
        return new BranchException("Branch with name " + name + " was not found.");
    }

    public static BranchException illegalField(String name, String resource, String details) {
        return new BranchException(String.format("""
                At %s, bad/illegal request:
                Resource: %s
                Details:
                %s
                """, name, resource, details));
    }
}
