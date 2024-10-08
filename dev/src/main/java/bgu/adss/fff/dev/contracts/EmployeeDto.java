package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for employee details
 * @param id
 * @param name
 * @param roles
 * @param bankDetails
 */
public record EmployeeDto(
        @JsonProperty("id") long id,
        @JsonProperty("name") String name,
        @JsonProperty("roles") RoleDto[] roles,
        @JsonProperty("bankDetails") String bankDetails,
        @JsonProperty("branch") String branchName
) { }
