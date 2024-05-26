package bgu.adss.fff.dev.domain.models;

import bgu.adss.fff.dev.exceptions.EmployeeException;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

import static bgu.adss.fff.dev.domain.models.Constants.NOT_SET;

@Entity(name="terms")
public class EmploymentTerms {

    @Id
    private long id;
    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    @Column
    @NonNull
    private LocalDate startDate;
    /**
     * Type of job: full-time/part-time/ contract. Read more at: {@link JobType}.
     */
    @Column
    @NonNull
    private JobType jobType;
    /**
     * The expected/agreed on monthly salary. May be initiated with -1.
     */
    @Column
    private float monthlySalary;
    /**
     * The hourly rate of the employee which was agreed on. May be initiated with -1.<br>
     * However, {@code hourlyRate == monthlySalary == -1} <b>is not allowed</b>.
     */
    @Column
    private float hourlyRate;
    /**
     * The number of days off the employee has.
     */
    @Column
    private int daysOff;
    /**
     * The direct manager of the employee.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mgr_id", referencedColumnName = "id")
    private Employee manager;
    /**
     * Signifies the end of employment date, when not applicable will be null.
     */
    private LocalDate endDate;

    public EmploymentTerms() { }

    public EmploymentTerms(LocalDate startDate, JobType jobType, Employee manager,
                           float monthlySalary, float hourlyRate, int daysOff) {
        if(monthlySalary == hourlyRate && hourlyRate == NOT_SET) {
            throw new IllegalArgumentException("Salary / Rate must be specified!");
        }
        this.startDate = startDate;
        this.jobType = jobType;
        this.manager = manager;

        this.monthlySalary = monthlySalary;
        this.hourlyRate = hourlyRate;
        this.daysOff = daysOff;
    }

    /**
     * Gets the ID of the employee associated with this instance.
     *
     * @return ID of the employee associated with this instance.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the employee associated with this instance.
     *
     * @param id the ID of the employee associated with this instance.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the Employee instance that is associated to this instance.
     *
     * @return the Employee instance that is associated to this instance.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets the Employee instance that is associated to this instance.
     *
     * @param employee the Employee instance that is associated to this instance.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Gets the start date of the employment.
     *
     * @return the start date of the employment.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the employment.
     *
     * @param startDate the start date of the employment.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the job type of the employment.
     *
     * @return the job type of the employment.
     */
    public JobType getJobType() {
        return jobType;
    }

    /**
     * Sets the job type of the employment.
     *
     * @param jobType the new job type of the employment.
     */
    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    /**
     * Gets the monthly salary of the employee.
     *
     * @return the monthly salary of the employee.
     */
    public float getMonthlySalary() {
        return monthlySalary;
    }

    /**
     * Sets the monthly salary of the employee.
     *
     * @param monthlySalary the new monthly salary of the employee.
     * @throws EmployeeException if both monthlySalary and hourlyRate are not set.
     */
    public void setMonthlySalary(float monthlySalary) {
        if(monthlySalary == hourlyRate && hourlyRate == NOT_SET) {
            throw EmployeeException.illegalField(id, "monthlySalary",
                    "monthlySalary and hourlyRate cannot be both not set");
        }

        this.monthlySalary = monthlySalary;
    }

    /**
     * Gets the hourly rate of the employee.
     *
     * @return the hourly rate of the employee.
     */
    public float getHourlyRate() {
        return hourlyRate;
    }

    /**
     * Sets the hourly rate of the employee.
     *
     * @param hourlyRate the new hourly rate of the employee.
     * @throws IllegalArgumentException if both monthlySalary and hourlyRate are not set.
     */
    public void setHourlyRate(float hourlyRate) {
        if(monthlySalary == hourlyRate && hourlyRate == NOT_SET) {
            throw EmployeeException.illegalField(id, "hourlyRate",
                    "monthlySalary and hourlyRate cannot be both not set");
        }

        this.hourlyRate = hourlyRate;
    }

    /**
     * Gets the number of days off the employee has.
     *
     * @return the number of days off the employee has.
     */
    public int getDaysOff() {
        return daysOff;
    }

    /**
     * Sets the number of days off the employee has.
     *
     * @param daysOff the new number of days off the employee has.
     */
    public void setDaysOff(int daysOff) {
        this.daysOff = daysOff;
    }

    /**
     * Gets the direct manager of the employee. null if irrelevant.
     *
     * @return the direct manager of the employee.
     */
    public Employee getManager() {
        return manager;
    }

    /**
     * Sets the direct manager of the employee.
     *
     * @param manager the new direct manager of the employee.
     */
    public void setManager(Employee manager) {
        this.manager = manager;
    }

    /**
     * Gets the end date of the employment. null if irrelevant.
     *
     * @return the end date of the employment, or null if not applicable.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the employment.
     *
     * @param endDate the new end date of the employment.
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
