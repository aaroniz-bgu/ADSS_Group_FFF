package bgu.adss.fff.dev.contracts;

public record ErrorDetails(
        int status,
        String message,
        String information
) {
}
