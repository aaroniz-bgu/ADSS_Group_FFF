package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.FullEmployeeDto;
import bgu.adss.fff.dev.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    /**
     * Establishes a new employee in the system.
     * @param request The request containing the following data: <br>
     *                - {@code String name}: Employee's full name.<br>
     *                - {@code Role[]} roles: Array of roles assigned to the employee.<br>
     *                - {@code int bankId}: The identifier of the bank facilitating the employee's account.<br>
     *                - {@code int bankBranch}: The 3-digit identifier of the bank branch facilitating the employee's account.<br>
     *                - {@code int accountId}: The employee's bank account ID.<br>
     *                - {@code LocalDateTime startDate}: The date when the employee started employment.<br>
     *                - {@code JobType jobType}: Enum Job type, represented as an integer (0-2) for FT/PT/Contract.<br>
     *                - {@code float monthlySalary}: Monthly salary, or -1 if not paid monthly.<br>
     *                - {@code float hourlyRate}: Hourly rate, must not be -1 if monthlySalary is -1.<br>
     *                - {@code int daysOff}: Yearly days off.<br>
     * @return ResponseEntity containing the created employee if successful, or a bad request status if the input is invalid.
     */
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody FullEmployeeDto request) {
        return ResponseEntity.noContent().build();
    }

    /**
     * Fetches all employees in the system.
     * @return ResponseEntity containing an array of EmployeeDto or no content if none exist.
     */
    @GetMapping
    public ResponseEntity<EmployeeDto[]> getEmployees() {
        return ResponseEntity.noContent().build();
    }

    /**
     * Fetches an employee by their ID.
     * @param id The ID of the employee to be retrieved.
     * @return ResponseEntity containing the EmployeeDto or no content if the employee does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") long id) {
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing employee in the system.
     * @param id The ID of the employee to be updated.
     * @param request The request containing updated employee data: <br>
     *                - {@code String name}: Employee's full name.<br>
     *                - {@code Role[]} roles: Array of roles assigned to the employee.<br>
     *                - {@code int bankId}: The identifier of the bank facilitating the employee's account.<br>
     *                - {@code int bankBranch}: The 3-digit identifier of the bank branch facilitating the employee's account.<br>
     *                - {@code int accountId}: The employee's bank account ID.<br>
     * @return ResponseEntity containing the updated employee if successful, or a bad request status if the input is invalid.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable long id, @RequestBody EmployeeDto request) {
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes an employee associated with the given ID from the system.
     * It is highly discouraged to use this option.
     * @param id Employee's ID (9 digits, same as on their ID card).
     * @return No content response if successful, or not found if the employee does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") long id) {
        return ResponseEntity.noContent().build();
    }
}
