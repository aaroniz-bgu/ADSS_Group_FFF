package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Contract for requesting a report for a specific shift
 * @param date
 * @param shift According to the {@link ShiftDayPart} enum
 */
public record ReportShiftRequest(
        @JsonProperty("date") LocalDateTime date,
        @JsonProperty("shift") int shift,
        @JsonProperty("enpId") long empId
        ) { }
