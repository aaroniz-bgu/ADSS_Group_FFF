package bgu.adss.fff.dev.domain.models;

import bgu.adss.fff.dev.data.HumanResourceConfig;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class Shift {
//    @EmbeddedId
    private EmbeddedShiftId id;
    private List<Employee> availableEmployees;
    private List<Employee> assignedEmployees;
    private List<Role> requiredRoles;
    private boolean isLocked;

    public Shift(LocalDateTime date, ShiftDayPart shift, boolean isLocked) {
        this.id = new EmbeddedShiftId(date, shift);
        this.isLocked = LocalDateTime.now()
                .isBefore(date.minus(HumanResourceConfig.barrierTime));

        this.availableEmployees = new ArrayList<>();
        this.assignedEmployees = new ArrayList<>();
        this.requiredRoles = new ArrayList<>();
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

    public List<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    public EmbeddedShiftId getId() {
        return id;
    }

    public List<Role> getRequiredRoles() {
        return requiredRoles;
    }

    public void setRequiredRoles(List<Role> requiredRoles) {
        this.requiredRoles = requiredRoles;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
