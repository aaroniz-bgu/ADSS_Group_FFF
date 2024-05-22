package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.EmployeeTermsDto;
import bgu.adss.fff.dev.contracts.FullEmployeeDto;
import bgu.adss.fff.dev.controllers.mappers.EmployeeMapper;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static bgu.adss.fff.dev.controllers.mappers.EmployeeMapper.map;

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
     *                - {@code long id}: Employee's state identifier.<br>
     *                - {@code String name}: Employee's full name.<br>
     *                - {@code Role[]} roles: Array of roles assigned to the employee.<br>
     *                - {@code int bankDetails}: Bank details of the employee supplied in th following manner:
     *                {@code bank_id:bank_branch:account_id}, where:<br>
     *                - - Bank ID - The identifier of the bank facilitating the employee's account.<br>
     *                - - Bank Branch The 3-digit identifier of the bank branch facilitating the employee's account.<br>
     *                - - Account ID - The employee's bank account ID.<br>
     *                - {@code LocalDateTime startDate}: The date when the employee started employment.<br>
     *                - {@code JobType jobType}: Enum Job type, represented as an integer (0-2) for FT/PT/Contract.<br>
     *                - {@code float monthlySalary}: Monthly salary, or -1 if not paid monthly.<br>
     *                - {@code float hourlyRate}: Hourly rate, must not be -1 if monthlySalary is -1.<br>
     *                - {@code int daysOff}: Yearly days off.<br>
     * @return ResponseEntity containing the created employee if successful, or a bad request status if the input is invalid.
     */
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody FullEmployeeDto request) {
        Employee emp = EmployeeMapper.fullMap(request);
        return new ResponseEntity<>(map(service.createEmployee(emp)), HttpStatus.CREATED);
    }

    /**
     * Fetches all employees in the system.
     * @return ResponseEntity containing an array of EmployeeDto or no content if none exist.
     */
    @GetMapping
    public ResponseEntity<EmployeeDto[]> getEmployees() {
        // Just to avoid casting, since java has a problem instantiating generic types:
        EmployeeDto[] objects = new EmployeeDto[0];

        return ResponseEntity.ok().body(service.getEmployees()
                .stream()
                .map(EmployeeMapper::map)
                .toList()
                .toArray(objects)
        );
    }

    /**
     * Fetches an employee by their ID.
     * @param id The ID of the employee to be retrieved.
     * @return ResponseEntity containing the EmployeeDto or no content if the employee does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") long id) {
        return ResponseEntity.ok(map(service.getEmployee(id)));
    }

    /**
     * Updates an existing employee in the system.
     * @param id The ID of the employee to be updated.
     * @param request The request containing updated employee data: <br>
     *                - {@code long id}: Employee's state identifier.<br>
     *                - {@code String name}: Employee's full name.<br>
     *                - {@code Role[]} roles: Array of roles assigned to the employee.<br>
     *                - {@code int bankDetails}: Bank details of the employee supplied in th following manner:
     *                {@code bank_id:bank_branch:account_id}, where:<br>
     *                - - Bank ID - The identifier of the bank facilitating the employee's account.<br>
     *                - - Bank Branch The 3-digit identifier of the bank branch facilitating the employee's account.<br>
     *                - - Account ID - The employee's bank account ID.<br>
     *
     * @return ResponseEntity containing the updated employee if successful, or a bad request status if the input is invalid.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable long id, @RequestBody EmployeeDto request) {
        service.updateEmployee(id, map(request));
        // https://stackoverflow.com/a/827045/19275130 :
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
        service.removeEmployee(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves the employment terms for an employee by their ID.
     * @param id The ID of the employee whose employment terms are to be retrieved.
     * @return ResponseEntity containing the employment terms of the employee if found, or no content if not found.
     */
    @GetMapping("/terms/{id}")
    public ResponseEntity<EmployeeTermsDto> getEmploymentTerms(@PathVariable("id") long id) {
        Employee res = service.getEmployee(id);
        return ResponseEntity.ok(map(res.getId(), res.getTerms()));
    }

    /**
     * Updates the employment terms for an employee in the system.
     * @param id The ID of the employee whose employment terms are to be updated.
     * @param request The request containing updated employment terms data: <br>
     *                - {@code LocalDateTime startDate}: Start date of employment.<br>
     *                - {@code JobType jobType}: Type of job (Full-time, Part-time, Contract).<br>
     *                - {@code float monthlySalary}: Monthly salary of the employee.<br>
     *                - {@code float hourlyRate}: Hourly rate of the employee.<br>
     *                - {@code int daysOff}: Number of days off per year.<br>
     *                - {@code EmployeeDto directManager}: Direct manager of the employee.<br>
     *                - {@code LocalDateTime endDate}: End date of employment.<br>
     * @return ResponseEntity containing the updated employment terms of the employee if successful, or no content if not found.
     */
    @PutMapping("/terms/{id}")
    public ResponseEntity<EmployeeTermsDto> updateEmploymentTerms(
            @PathVariable("id") long id,
            @RequestBody EmployeeTermsDto request){
        service.updateEmployementTerms(id, map(request));
        // https://stackoverflow.com/a/827045/19275130 :
        return ResponseEntity.noContent().build();
    }
}
