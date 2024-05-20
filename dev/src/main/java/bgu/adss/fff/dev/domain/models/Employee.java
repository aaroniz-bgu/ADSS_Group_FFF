package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.*;

import java.util.List;

@Entity(name="employee")
public class Employee {
    @Id
    private long id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
    private int bankId;
    private int bankBranch;
    private int accountId;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) // TODO MAP PK
    private EmploymentTerms terms;

    public Employee(long id, String name, List<Role> roles, EmploymentTerms terms,
                    int bankId, int bankBranch, int accountId) {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.terms = terms;

        this.bankId = bankId;
        this.bankBranch = bankBranch;
        this.accountId = accountId;
    }

    /**
     * Gets the unique identifier of the employee.
     *
     * @return The ID of the employee.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the name of the employee.
     *
     * @return The name of the employee.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the employee.
     *
     * @param name The new name of the employee.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the list of roles assigned to the employee.
     *
     * @return The list of roles of the employee.
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Sets the list of roles for the employee.
     *
     * @param roles The new list of roles for the employee.
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * Gets the bank account details of the employee.
     *
     * @return The bank account details of the employee.
     */
    public String getBank() {
        return bankId + ":" + bankBranch + ":" + accountId;
    }

    /**
     * Sets the bank account details for the employee.
     *
     * @param bankId The official state bank id.
     * @param bankBranch The branch which the employee's account is stored at.
     * @param accountId The id of the employee's account.
     */
    public void setBank(int bankId, int bankBranch, int accountId) {
        this.bankId = bankId;
        this.bankBranch = bankBranch;
        this.accountId = accountId;
    }

    /**
     * Gets the employment terms of the employee.
     *
     * @return The employment terms of the employee.
     */
    public EmploymentTerms getTerms() {
        return terms;
    }

    /**
     * Sets the employment terms for the employee.
     *
     * @param terms The new employment terms for the employee.
     */
    public void setTerms(EmploymentTerms terms) {
        this.terms = terms;
    }
}
