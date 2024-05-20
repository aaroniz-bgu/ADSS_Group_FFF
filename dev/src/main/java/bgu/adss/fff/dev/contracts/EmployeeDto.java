package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Contract for employee details
 * @param id
 * @param name
 * @param roles
 * @param bankId
 * @param bankBranch
 * @param accountId
 */
public record EmployeeDto(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("roles") List<RoleDto> roles,
        @JsonProperty("bankId") int bankId,
        @JsonProperty("bankBranch") int bankBranch,
        @JsonProperty("accountId") int accountId
) {
    public EmployeeDto {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Invalid id");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }
        // TODO Decide if this is necessary
//        if (roles == null || roles.isEmpty()) {
//            throw new IllegalArgumentException("Roles cannot be empty");
//        }
        if (bankId <= 0) {
            throw new IllegalArgumentException("Invalid bankId");
        }
        if (bankBranch <= 0) {
            throw new IllegalArgumentException("Invalid bankBranch");
        }
        if (accountId <= 0) {
            throw new IllegalArgumentException("Invalid accountId");
        }
    }
}
