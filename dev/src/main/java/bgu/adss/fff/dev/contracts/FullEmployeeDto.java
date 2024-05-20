package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for the full employee details
 * @param employee Employee details, using {@link EmployeeDto}
 * @param terms Employment terms, using {@link EmployeeTermsDto}
 */
public record FullEmployeeDto(
        @JsonProperty("employee") EmployeeDto employee,
        @JsonProperty("terms") EmployeeTermsDto terms
) {
    public FullEmployeeDto {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        // TODO Decide if this is necessary
//        if (terms == null) {
//            throw new IllegalArgumentException("Terms cannot be null");
//        }
    }
}
