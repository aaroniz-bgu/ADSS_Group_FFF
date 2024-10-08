package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class EmbeddedShiftId implements Serializable {
    private LocalDate date;
    private ShiftDayPart shift;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_name")
    private Branch branch;

    public EmbeddedShiftId(LocalDate date, ShiftDayPart shift, Branch branch) {
        this.date = date;
        this.shift = shift;
        this.branch = branch;
    }

    public EmbeddedShiftId() {

    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ShiftDayPart getShift() {
        return shift;
    }

    public void setShift(ShiftDayPart shift) {
        this.shift = shift;
    }

    public Branch getBranch() { return branch; }

    public void setBranch(Branch branch) { this.branch = branch; }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof EmbeddedShiftId other) {
            return date.equals(other.date) && (shift == other.shift) && branch.equals(other.branch);
        }
        return false;
    }
}
