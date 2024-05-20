package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Contract for a shift
 * @param date The date of the shift
 * @param shift According to the {@link ShiftDayPart} enum
 * @param isLocked True if the shift is locked and not prone to changes
 * @param availableEmployees Employees available for the shift
 * @param assignedEmployees Employees assigned to the shift
 * @param requiredRoles The roles required for the shift
 */
public record ShiftDto(
        @JsonProperty("date") LocalDateTime date,
        @JsonProperty("shift") ShiftDayPart shift, // Todo import the enum
        @JsonProperty("isLocked") boolean isLocked,
        @JsonProperty("availableEmployees") EmployeeDto[] availableEmployees,
        @JsonProperty("assignedEmployees") EmployeeDto[] assignedEmployees,
        @JsonProperty("requiredRoles") RoleDto[] requiredRoles
        ) {
}
