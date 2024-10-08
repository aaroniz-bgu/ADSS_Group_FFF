package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Contract for a shift
 * @param date The date of the shift
 * @param shift According to the {@link ShiftDayPart} enum
 * @param branch The branch name
 * @param isLocked True if the shift is locked and not prone to changes
 * @param availableEmployees Employees available for the shift
 * @param assignedEmployees Employees assigned to the shift
 * @param requiredRoles The roles required for the shift
 */
public record ShiftDto(
        @JsonProperty("date") LocalDate date,
        @JsonProperty("shift") int shift,
        @JsonProperty("branch") String branch,
        @JsonProperty("isLocked") boolean isLocked,
        @JsonProperty("availableEmployees") EmployeeDto[] availableEmployees,
        @JsonProperty("assignedEmployees") EmployeeDto[] assignedEmployees,
        @JsonProperty("requiredRoles") String[] requiredRoles
        ) { }
