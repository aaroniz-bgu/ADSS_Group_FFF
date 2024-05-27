package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Contract for requiring a role for a specific shift
 * Used for both adding and removing roles
 * @param date The date of the shift
 * @param shift According to the {@link ShiftDayPart} enum ordinal
 * @param role The roles to add or remove
 * <!--@param addOrRemove True for add, false for remove-->
 */
public record RequireRoleRequest(
        @JsonProperty("date") LocalDate date,
        @JsonProperty("shift") int shift,
        @JsonProperty("roles") String role
        ) { }
