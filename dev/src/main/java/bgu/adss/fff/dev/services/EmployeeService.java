package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.EmploymentTerms;

import java.util.Collection;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    Collection<Employee> getEmployees();
    Employee getEmployee(long id);
    Employee updateEmployee(long id, Employee employee);
    void removeEmployee(long id);

    Employee updateEmployementTerms(long id, EmploymentTerms terms);
}
