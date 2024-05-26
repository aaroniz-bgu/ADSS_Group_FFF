package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import com.fasterxml.jackson.annotation.JsonProperty;
// TODO import the ShiftDayPart enum

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Contract for requiring a role for a specific shift
 * Used for both adding and removing roles
 * @param date The date of the shift
 * @param shift According to the {@link ShiftDayPart} enum
 * @param roles The roles to add or remove
 * <!--@param addOrRemove True for add, false for remove-->
 */
public record RequireRoleRequest(
        @JsonProperty("date") LocalDateTime date,
        @JsonProperty("shift") ShiftDayPart shift,
        @JsonProperty("roles") RoleDto[] roles
        // @JsonProperty("addOrRemove") boolean addOrRemove // True for add, false for remove
        ) { }
