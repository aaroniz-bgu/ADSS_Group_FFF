package bgu.adss.fff.dev.contracts;

/**
 * Contract for Error details
 * @param status
 * @param message
 * @param information
 */
public record ErrorDetails(
        int status,
        String message,
        String information
) {
}