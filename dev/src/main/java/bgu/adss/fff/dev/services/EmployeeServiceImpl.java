package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.EmployeeRepository;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.EmploymentTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

import static bgu.adss.fff.dev.domain.models.Constants.NOT_SET;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        if(repository.existsById(employee.getId())) {
            // TODO Throw...
        }
        if(!(checkEmployeeFields(employee) && checkTermsField(employee.getTerms())))  {
            // TODO Throw...
        }
        return repository.save(employee);
    }

    /**
     * Checks whether the employee's name is set properly and possibly more attributes to ensure validity.
     * @param employee The employee to check.
     * @return true if and only if the employee is well set.
     */
    private boolean checkEmployeeFields(Employee employee) {
        String exp = "^[a-zA-Z\\\\s]+";
        return employee != null && employee.getName().matches(exp);
    }

    /**
     * Validation check of the employment terms fed by the client to ensure data integrity.
     * Check whether start date & job type are set, if payment details are defined and whether the
     * days off is non-negative value.
     * @param terms The terms to be checked.
     * @return true if and only if the terms are valid.
     */
    private boolean checkTermsField(EmploymentTerms terms) {
        boolean resources = terms != null && terms.getStartDate() != null && terms.getJobType() != null;
        boolean payment = resources && (terms.getHourlyRate() != NOT_SET || terms.getMonthlySalary() != NOT_SET);
        return resources && payment && terms.getDaysOff() > 0;
    }

    @Override
    public Collection<Employee> getEmployees() {
        return repository.findAll();
    }

    @Override
    public Optional<Employee> getEmployee(long id) {
        return repository.findById(id);
    }

    @Override
    public Employee updateEmployee(long id, Employee employee) {
        return null;
    }

    @Override
    public void removeEmployee(long id) {
        if(!repository.existsById(id)) {
            // TODO Throw...
        }
        repository.deleteById(id);
    }

    @Override
    public Employee updateEmployementTerms(long id, EmploymentTerms terms) {
        Employee emp = repository.findById(id).orElseThrow(
                () -> new RuntimeException(""));
        if(!checkTermsField(terms)) {
            // TODO Throw...
        }
        emp.setTerms(terms);
        repository.save(emp);
        return emp;
    }
}
