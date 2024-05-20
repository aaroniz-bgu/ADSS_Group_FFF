package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for the role of an employee
 * @param name
 * @param isShiftManager
 */
public record RoleDto(
        @JsonProperty("name") String name,
        @JsonProperty("isShiftManager") boolean isShiftManager
        // @JsonProperty("isHrManager") boolean isHrManager
) {
    public RoleDto {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }
    }
}
