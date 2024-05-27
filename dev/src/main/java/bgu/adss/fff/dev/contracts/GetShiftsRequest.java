package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Represents a time period where shifts will be returned.
 */
public record GetShiftsRequest(
        @JsonProperty("from") LocalDate from,
        @JsonProperty("to") LocalDate to
        ) { }
