package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.JobType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Contract for employee terms
 * The employee should either have a monthly salary or an hourly rate, but not both
 * @param id
 * @param startDate Should not be null
 * @param jobType According to the {@link JobType} enum
 * @param monthlySalary
 * @param hourlyRate
 * @param daysOff
 * @param directManager if the employee has no direct manager, this should be null
 * @param endDate Should be null if the employee is still active
 */
public record EmployeeTermsDto(
        @JsonProperty("id") Long id,
        @JsonProperty("startDate") LocalDateTime startDate,
        @JsonProperty("jobType") JobType jobType,
        @JsonProperty("monthlySalary") float monthlySalary,
        @JsonProperty("HourlyRate") float hourlyRate,
        @JsonProperty("daysOff") int daysOff,
        @JsonProperty("directManager") EmployeeDto directManager,
        @JsonProperty("endDate") LocalDateTime endDate
        ) { }
