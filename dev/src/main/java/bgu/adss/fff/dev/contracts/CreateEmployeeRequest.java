package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for creating a new employee
 * @param employee Employee details, using {@link EmployeeDto}
 * @param terms Employee terms, using {@link EmployeeTermsDto} //TODO Decide if this can be empty
 */
public record CreateEmployeeRequest(
        @JsonProperty("employee") EmployeeDto employee,
        @JsonProperty("terms") EmployeeTermsDto terms
) {
public CreateEmployeeRequest {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        // TODO Decide if this is necessary
//        if (terms == null) {
//            throw new IllegalArgumentException("Terms cannot be null");
//        }
    }
}
