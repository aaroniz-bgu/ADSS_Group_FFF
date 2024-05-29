package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Represents a time period where shifts will be returned in addition to the branch name
 * TODO decide maybe if the branch name is empty, should we return all the shifts from all the branches?
 */
public record GetShiftsRequest(
        @JsonProperty("from") LocalDate from,
        @JsonProperty("to") LocalDate to,
        @JsonProperty("branch") String branchName
        ) { }
