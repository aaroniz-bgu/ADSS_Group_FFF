package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Contract for requiring a role for a specific shift
 * Used for both adding and removing roles
 * @param date The date of the shift
 * @param shift According to the {@link ShiftDayPart} enum ordinal
 * @param branch The branch name
 * @param role The roles to add or remove
 * @param reoccurring whether to delete only once or add as a reoccurring required role.
 * <!--@param addOrRemove True for add, false for remove-->
 */
public record RequireRoleRequest(
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("date") LocalDate date,
        @JsonProperty("shift") int shift,
        @JsonProperty("branch") String branch,
        @JsonProperty("roles") String role,
        @JsonProperty("reoccurring") boolean reoccurring
        ) { }
