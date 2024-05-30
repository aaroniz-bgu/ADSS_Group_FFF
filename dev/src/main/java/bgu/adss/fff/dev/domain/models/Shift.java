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

    // For JDA:
    public Shift() { }

    public Shift(LocalDate date, ShiftDayPart shift, Branch branch) {
        //        this.isLocked = LocalDate.now() atm like this...
        //                .isBefore(date.minus(HumanResourceConfig.barrierTime));
        this(date, shift, date.isBefore(LocalDate.now()), branch);
    }

    public Shift(LocalDate date, ShiftDayPart shift, boolean isLocked, Branch branch) {
        this.id = new EmbeddedShiftId(date, shift, branch);
        this.isLocked = isLocked;

        this.availableEmployees = new ArrayList<>();
        this.assignedEmployees = new ArrayList<>();
        this.requiredRoles = new ArrayList<>();
    }

    public void addOrRemoveAvailableEmployee(Employee emp) {
        if(emp == null) {
            throw new NullPointerException("Cannot update null employee availability.");
        }

        if(!availableEmployees.contains(emp)) {
            availableEmployees.add(emp);
        } else {
            availableEmployees.remove(emp);
        }
    }

    public void addRequiredRole(Role role) {
        if(role == null) {
            throw new NullPointerException("Cannot add null role.");
        }

        if(!requiredRoles.contains(role)) {
            requiredRoles.add(role);
        }
    }

    public void removeRequiredRole(Role role) {
        if(role == null) {
            throw new NullPointerException("Cannot add null role.");
        }
        requiredRoles.remove(role);
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

    public void setAssignedEmployees(List<Employee> assignedEmployees) {
        this.assignedEmployees = assignedEmployees;
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

    public String getBranchName() { return id.getBranch().getName(); }
}
