package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.JobType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Contract for the full employee details
 */
public record FullEmployeeDto(
        @JsonProperty("id") long id,
        @JsonProperty("name") String name,
        @JsonProperty("roles") RoleDto[] roles,
        @JsonProperty("bankDetails") String bankDetails,
        @JsonProperty("branch") String branchName,
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("startDate") LocalDate startDate,
        @JsonProperty("jobType") int jobType,
        @JsonProperty("monthlySalary") float monthlySalary,
        @JsonProperty("HourlyRate") float hourlyRate,
        @JsonProperty("daysOff") int daysOff,
        @JsonProperty("directManager") EmployeeDto directManager,
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("endDate") LocalDate endDate
) { }
