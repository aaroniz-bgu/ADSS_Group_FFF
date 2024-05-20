package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
// TODO import the ShiftDayPart enum

/**
 * Contract for requesting a report for a specific shift
 * @param date
 * @param shift According to the {@link ShiftDayPart} enum
 */
public record ReportShiftRequest(
        @JsonProperty("date") LocalDateTime date,
        @JsonProperty("shift") ShiftDayPart shift // Todo import the enum
        ) {
    public ReportShiftRequest {
        if (date == null) {
            throw new IllegalArgumentException("Invalid date");
        }
        if (shift == null) {
            throw new IllegalArgumentException("Invalid shift");
        }
    }
}
