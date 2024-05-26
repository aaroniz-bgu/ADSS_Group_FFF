package bgu.adss.fff.dev.exceptions;

public class RoleException extends RuntimeException{
    private RoleException(String msg) { super(msg); }

    public static RoleException alreadyExists(String name) {
        return new RoleException("Role with name " + name + " already exists.");
    }

    public static RoleException notFound(String name) {
        return new RoleException("Role with name " + name + " was not found.");
    }

    public static RoleException illegalField(String name, String resource, String details) {
        return new RoleException(String.format("""
                At %s, bad/illegal request:
                Resource: %s
                Details:
                %s
                """, name, resource, details));
    }
}
