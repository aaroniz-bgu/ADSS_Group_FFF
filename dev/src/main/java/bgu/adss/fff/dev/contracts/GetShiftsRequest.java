package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Represents a time period where shifts will be returned in addition to the branch name
 * Branch is optional and if not provided, all branches will be considered
 */
public record GetShiftsRequest(
        @JsonProperty("from") LocalDate from,
        @JsonProperty("to") LocalDate to,
        @JsonProperty("branch") String branchName
        ) { }
