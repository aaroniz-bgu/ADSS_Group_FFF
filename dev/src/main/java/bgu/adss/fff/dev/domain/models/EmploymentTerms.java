package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;

@Entity(name="terms")
public class EmploymentTerms {
//FIXME    /**
//     * The employee which is mapped to this terms object.
//     */
//    private Employee employee;
    /**
     * The date when the employee was employed.
     */
    private LocalDateTime startDate;
    /**
     * Type of job: full-time/part-time/ contract. Read more at: {@link JobType}.
     */
    private JobType jobType;
    /**
     * The expected/agreed on monthly salary. May be initiated with -1.
     */
    private float monthlySalary;
    /**
     * The hourly rate of the employee which was agreed on. May be initiated with -1.<br>
     * However, {@code hourlyRate == monthlySalary == -1} <b>is not allowed</b>.
     */
    private float hourlyRate;
    /**
     * The number of days off the employee has.
     */
    private int daysOff;
    /**
     * The direct manager of the employee.
     */
    private Employee manager;
    /**
     * Signifies the end of employment date, when not applicable will be null.
     */
    private LocalDateTime endDate;

    public EmploymentTerms(LocalDateTime startDate, JobType jobType, Employee manager,
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public float getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(float monthlySalary) {
        if(monthlySalary == hourlyRate && hourlyRate == NOT_SET) {
            throw new IllegalArgumentException("Salary / Rate must be specified!");
        }

        this.monthlySalary = monthlySalary;
    }

    public float getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(float hourlyRate) {
        if(monthlySalary == hourlyRate && hourlyRate == NOT_SET) {
            throw new IllegalArgumentException("Salary / Rate must be specified!");
        }

        this.hourlyRate = hourlyRate;
    }

    public int getDaysOff() {
        return daysOff;
    }

    public void setDaysOff(int daysOff) {
        this.daysOff = daysOff;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
