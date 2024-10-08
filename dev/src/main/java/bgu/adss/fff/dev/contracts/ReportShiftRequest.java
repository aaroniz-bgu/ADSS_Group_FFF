package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Contract for requesting a report for a specific shift
 * @param date
 * @param shift According to the {@link ShiftDayPart} enum
 */
public record ReportShiftRequest(
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("date") LocalDate date,
        @JsonProperty("shift") int shift,
        @JsonProperty("empId") long empId
        ) { }
