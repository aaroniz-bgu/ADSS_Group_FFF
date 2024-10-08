package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.List;

@Entity(name="employee")
public class Employee implements Serializable {
    @Id
    private long id;
    @Column
    @NonNull
    private String name;
    @Column
    private int bankId;
    @Column
    private int bankBranch;
    @Column
    private int accountId;

    /**
     * Roles-Employee relation is managed by Employee.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "emps",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "name")
    )
    private List<Role> roles;
    /**
     * The terms of the employment
     */
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "employee")
    private EmploymentTerms terms;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_name")
    private Branch branch;

    // For JPA:
    public Employee() { }

    public Employee(long id, String name, List<Role> roles, EmploymentTerms terms,
                    int bankId, int bankBranch, int accountId, Branch branch) {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.terms = terms;

        this.bankId = bankId;
        this.bankBranch = bankBranch;
        this.accountId = accountId;

        this.branch = branch;
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

    /**
     * Gets the branch the employee works at.
     *
     * @return The branch the employee works at.
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     * Sets the branch the employee works at.
     * @param branch The new branch the employee works at.
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}
