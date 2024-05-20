package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.JobType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contract for the full employee details
 */
public record FullEmployeeDto(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("roles") RoleDto[] roles,
        @JsonProperty("bankId") int bankId,
        @JsonProperty("bankBranch") int bankBranch,
        @JsonProperty("accountId") int accountId,
        @JsonProperty("startDate") LocalDateTime startDate,
        @JsonProperty("jobType") JobType jobType,
        @JsonProperty("monthlySalary") float monthlySalary,
        @JsonProperty("HourlyRate") float hourlyRate,
        @JsonProperty("daysOff") int daysOff,
        @JsonProperty("directManager") EmployeeDto directManager,
        @JsonProperty("endDate") LocalDateTime endDate
) { }
