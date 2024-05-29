package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.EmployeeRepository;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.EmploymentTerms;
import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.exceptions.EmployeeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static bgu.adss.fff.dev.domain.models.Constants.*;
import static bgu.adss.fff.dev.util.EmployeeUtilHelper.getBankDetailsHelper;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repository;
    private final RoleService roleService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository,
                               RoleService roleService) {
        this.repository = repository;
        this.roleService = roleService;
    }

    /**
     * Creates a new employee in the system.
     * @param employee The employee to create.
     * @return The created employee.
     * @throws EmployeeException if the employee already exists or the employee data is invalid.
     */
    @Override
    public Employee createEmployee(Employee employee) {
        if(repository.existsById(employee.getId())) {
            throw EmployeeException.alreadyExists(employee.getId());
        }
        if(!(checkEmployeeFields(employee) && checkTermsField(employee.getTerms())))  {
            throw EmployeeException.illegalField(employee.getId(),
                    "Employee", "Bad request, can be any of the following:");
        }
        List<Role> roles = getRolesHelper(employee);

        employee.setRoles(roles);
        employee.getTerms().setManager(getManager(employee));
        return repository.save(employee);
    }

    /**
     * Checks whether the employee's name is set properly and possibly more attributes to ensure validity.
     * @param employee The employee to check.
     * @return true if and only if the employee is well set.
     */
    private boolean checkEmployeeFields(Employee employee) {
        String exp = "^[a-zA-Z\\s]+";
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
    public List<Employee> getEmployees() {
        return repository.findAll();
    }

    @Override
    public Employee getEmployee(long id) {
        return repository.findById(id).orElseThrow(() -> EmployeeException.notFound(id));
    }

    /**
     * Updates the employee with the given id with the new employee data.
     * @param id The id of the employee to update.
     * @param employee The new employee data.
     * @return The updated employee.
     * @throws EmployeeException if the employee with the given id doesn't exist or the employee data is invalid.
     */
    @Override
    public Employee updateEmployee(long id, Employee employee) throws EmployeeException {
        if(!(checkEmployeeFields(employee) && employee.getId() == id) ) {
            throw EmployeeException.illegalField(id, "Employee",
                    "Either name/instance we're illegal or id mismatched");
        }

        Employee toUpdate = getEmployee(id);
        toUpdate.setName(employee.getName());

        List<Role> roles = getRolesHelper(employee);

        toUpdate.setRoles(roles);

        int[] bank = getBankDetailsHelper(employee.getBank());
        toUpdate.setBank(bank[BANK_ID_IND], bank[BANK_BRANCH_IND], bank[ACCOUNT_ID_IND]);

        return repository.save(toUpdate);
    }

    /**
     * A list of all the roles of the employee from the db if there is a matching role.
     * @param employee The employee to get the roles from.
     * @return List of roles if all exist, null otherwise.
     * @throws EmployeeException if one or more of the roles do not exist in the system.
     */
    private List<Role> getRolesHelper(Employee employee) throws EmployeeException {
        List<String> roleNames = employee.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        List<Role> roles = roleService.returnIfExists(roleNames);
        if (roles == null) {
            throw EmployeeException.illegalField(employee.getId(), "Employee",
                    "One or more of the given roles do not exist in the system.");
        }
        return roles;
    }

    @Override
    public void removeEmployee(long id) {
        if(!repository.existsById(id)) {
            throw EmployeeException.notFound(id);
        }
        repository.deleteById(id);
    }

    @Override
    public Employee updateEmployementTerms(long id, EmploymentTerms terms) {
        Employee emp = getEmployee(id);

        if(!checkTermsField(terms)) {
            throw EmployeeException.illegalField(id, "Terms",
                    "Either starting date/ job type missing or payment / days off are illegal");
        }

        Employee mgr = getManager(emp);
        terms.setManager(mgr);
        emp.setTerms(terms);
        repository.save(emp);
        return emp;
    }

    /**
     * Gets the manager of the given employee which was supplied by the service layer.
     * @param employee The employee which was supplied by the service layer.
     * @return The manager instance of this employee or null if it doesn't have one.
     * @throws EmployeeException if employee with the given id doesn't exist.
     */
    private Employee getManager(Employee employee) {
        Employee mgr;
        if((mgr = employee.getTerms().getManager()) != null) {
            long mgrId = mgr.getId();
            return repository.findById(mgrId).orElseThrow(
                    () -> EmployeeException.illegalField(employee.getId(),
                            "Terms:Manager", "Not exists manager with id " + mgrId)
            );
        }
        return null;
    }
}
