package bgu.adss.fff.dev.domain.models;

import bgu.adss.fff.dev.data.HumanResourceConfig;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "shifts")
public class Shift {
    @EmbeddedId
    private EmbeddedShiftId id;
    @Column
    private boolean isLocked;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "available_emps",
            joinColumns = {
                    @JoinColumn(name = "shift_date"),
                    @JoinColumn(name = "day_part")
            },
            inverseJoinColumns = @JoinColumn(name = "emp_id")
    )
    private List<Employee> availableEmployees;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "assigned_emps",
            joinColumns = {
                    @JoinColumn(name = "shift_date"),
                    @JoinColumn(name = "day_part")
            },
            inverseJoinColumns = @JoinColumn(name = "emp_id")
    )
    private List<Employee> assignedEmployees;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "req_roles",
            joinColumns = {
                @JoinColumn(name = "shift_date"),
                @JoinColumn(name = "day_part")
            },
            inverseJoinColumns = @JoinColumn(name = "role")
    )
    private List<Role> requiredRoles;

    // For JPA:
    public Shift() { }

    public Shift(LocalDate date, ShiftDayPart shift, boolean isLocked) {
        this.id = new EmbeddedShiftId(date, shift);
        this.isLocked = LocalDate.now()
                .isBefore(date.minus(HumanResourceConfig.barrierTime));

        this.availableEmployees = new ArrayList<>();
        this.assignedEmployees = new ArrayList<>();
        this.requiredRoles = new ArrayList<>();
    }

    public EmbeddedShiftId getEmbeddedId() {
        return id;
    }

    public LocalDate getDate() {
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
