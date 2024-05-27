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

    public ShiftRoleRequirement(DayOfWeek day, ShiftDayPart part, Role role) {
        this.id = new ShiftRoleRequirementId(day, part, role);
    }

    public DayOfWeek getDay() {
        return id.day;
    }

    public void setDay(DayOfWeek day) {
        this.id.day = day;
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

    @Embeddable
    class ShiftRoleRequirementId implements Serializable {
        private DayOfWeek day;
        private ShiftDayPart part;
        @ManyToOne
        private Role role;

        public ShiftRoleRequirementId() {
        }

        public ShiftRoleRequirementId(DayOfWeek day, ShiftDayPart part, Role role) {
            this.day = day;
            this.part = part;
            this.role = role;
        }
    }
}
