package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.EmployeeRepository;
import bgu.adss.fff.dev.data.RoleFieldRepository;
import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.EmploymentTerms;
import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.domain.models.RoleField;
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
    private final RoleFieldRepository fieldRepository;
    private final RoleService roleService;
    private final BranchService branchService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository,
                               RoleFieldRepository fieldRepository,
                               RoleService roleService,
                               BranchService branchService) {
        this.repository = repository;
        this.fieldRepository = fieldRepository;
        this.roleService = roleService;
        this.branchService = branchService;
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
        employee.getTerms().setEmployee(employee);
        // Set the branch of the employee to the branch instance from the db, since the branch name is the only
        // thing that is supplied by the client.
        employee.setBranch(branchService.getBranch(employee.getBranch().getName()));
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

    /**
     * Gets all employees in the system that are assigned to the given branch.
     * @param branch The branch to get the employees from.
     * @return A list of all employees in the system that are assigned to the given branch.
     */
    @Override
    public List<Employee> getEmployeesByBranch(Branch branch) {
        return repository.findEmployeesByBranch(branchService.getBranch(branch.getName()));
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

        toUpdate.setBranch(branchService.getBranch(employee.getBranch().getName()));

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
        terms.setEmployee(emp);
        terms.setManager(mgr);
        emp.setTerms(terms);
        repository.save(emp);
        return emp;
    }

    /**
     * Updates/ creates a field for the given employee, with the given role.
     *
     * @param emp   The employee which associated with this field, for which it's need to be updated.
     * @param role  The role which is associated with this custom field.
     * @param field The name of the custom field which has to be updated.
     * @param val   The updated value which will be saved.
     * @return for testing, but basically the new field object created.
     */
    @Override
    public RoleField updateCustomField(Employee emp, Role role, String field, String val) {
        // Retrieve resources:
        long empId = emp.getId();
        emp = repository.findById(empId).orElseThrow(() -> EmployeeException.notFound(empId));
        role = roleService.getRole(role.getName());

        field = field.toLowerCase();
        val = val.toLowerCase();

        if(!emp.getRoles().contains(role)) throw EmployeeException.illegalField(
                empId, "role: "+role.getName(), "Role doesn't exist for this employee");

        RoleField.RoleFieldKey fieldId = new RoleField.RoleFieldKey(emp, role, field);
        RoleField fieldObj = fieldRepository.findById(fieldId).orElse(new RoleField(fieldId, val));
        fieldObj.setValue(val);
        return fieldRepository.save(fieldObj);
    }

    /**
     * Retrieves a field of the given employee and role.
     *
     * @param emp The employee.
     * @param role The role.
     * @param field The field's name.
     * @return RoleField Object.
     */
    @Override
    public RoleField getCustomField(Employee emp, Role role, String field) {
        // Retrieve resources:
        long empId = emp.getId();
        emp = repository.findById(empId).orElseThrow(() -> EmployeeException.notFound(empId));
        role = roleService.getRole(role.getName());

        field = field.toLowerCase();
        String finalField = field;

        if(!emp.getRoles().contains(role)) throw EmployeeException.illegalField(
                empId, "role: " + role.getName(), "Role doesn't exist for this employee");

        return fieldRepository.findById(new RoleField.RoleFieldKey(emp, role, field)).orElseThrow(
                () -> EmployeeException.illegalField(empId, "field: "+ finalField,
                        "This field was never saved/ given to this employee, therefore cannot be found.")
        );
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
