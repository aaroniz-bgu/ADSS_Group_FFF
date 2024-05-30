package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.DayOfWeek;

/**
 * Represent s a reoccurring role constraint on shifts.
 */
@Entity
public class ShiftRoleRequirement {

    @EmbeddedId
    private ShiftRoleRequirementId id;

    public ShiftRoleRequirement() { }

    public ShiftRoleRequirement(DayOfWeek day, ShiftDayPart part, Role role, Branch branch) {
        this.id = new ShiftRoleRequirementId(day, part, role, branch);
    }

    public DayOfWeek getDay() {
        return id.weekDay;
    }

    public void setDay(DayOfWeek day) {
        this.id.weekDay = day;
    }

    public ShiftDayPart getPart() {
        return id.part;
    }

    public void setPart(ShiftDayPart part) {
        this.id.part = part;
    }

    public Role getRole() {
        return id.role;
    }

    public void setRole(Role role) {
        this.id.role = role;
    }

    public Branch getBranch() { return id.branch; }

    public void setBranch(Branch branch) { this.id.branch = branch; }

    @Embeddable
    public static class ShiftRoleRequirementId implements Serializable {
        private DayOfWeek weekDay;
        private ShiftDayPart part;
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "branch_name")
        private Branch branch;
        @ManyToOne
        private Role role;

        public ShiftRoleRequirementId() {
        }

        public ShiftRoleRequirementId(DayOfWeek day, ShiftDayPart part, Role role, Branch branch) {
            this.weekDay = day;
            this.part = part;
            this.branch = branch;
            this.role = role;
        }
    }
}
