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
        @JsonProperty("roles") RoleDto[] roles,
        @JsonProperty("bankId") int bankId,
        @JsonProperty("bankBranch") int bankBranch,
        @JsonProperty("accountId") int accountId
) { }
