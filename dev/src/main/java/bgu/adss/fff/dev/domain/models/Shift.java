package bgu.adss.fff.dev.domain.models;

import bgu.adss.fff.dev.data.HumanResourceConfig;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Shift {
    @EmbeddedId
    private EmbeddedShiftId id;
    private List<Employee> availableEmployees;
    private List<Employee> assignedEmployees;
    private boolean isLocked;

    public Shift(LocalDateTime date, ShiftDayPart shift,
                 List<Employee> availableEmployees,
                 List<Employee> assignedEmployees,
                 boolean isLocked) {
        this.id = new EmbeddedShiftId(date, shift);
        this.availableEmployees = availableEmployees;
        this.assignedEmployees = assignedEmployees;
        this.isLocked = LocalDateTime.now()
                .isBefore(date.minus(HumanResourceConfig.barrierTime));
    }

    public EmbeddedShiftId getEmbeddedId() {
        return id;
    }

    public LocalDateTime getDate() {
        return id.getDate();
    }

    public ShiftDayPart getShiftDayPart() {
        return id.getShift();
    }

    public List<Employee> getAvailableEmployees() {
        return availableEmployees;
    }

    public void setAvailableEmployees(List<Employee> availableEmployees) {
        this.availableEmployees = availableEmployees;
    }

    public List<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void setAssignedEmployees(List<Employee> assignedEmployees) {
        this.assignedEmployees = assignedEmployees;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
