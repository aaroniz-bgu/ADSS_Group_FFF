package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.EmploymentTerms;
import bgu.adss.fff.dev.domain.models.RoleField;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    List<Employee> getEmployees();
    List<Employee> getEmployeesByBranch(Branch branch);
    Employee getEmployee(long id);
    Employee updateEmployee(long id, Employee employee);
    void removeEmployee(long id);

    Employee updateEmployementTerms(long id, EmploymentTerms terms);

    RoleField updateCustomField(long empId, String roleName, String field, String val);
    RoleField getCustomField(long empId, String roleName, String field);
}
