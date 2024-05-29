package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class EmbeddedShiftId implements Serializable {
    private LocalDate date;
    private ShiftDayPart shift;
    private String branchName;

    public EmbeddedShiftId(LocalDate date, ShiftDayPart shift, String branch) {
        this.date = date;
        this.shift = shift;
        this.branchName = branch;
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

    public String getBranch() { return branchName; }

    public void setBranch(String branch) { this.branchName = branch; }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof EmbeddedShiftId other) {
            return date.equals(other.date) && (shift == other.shift) && branchName.equals(other.branchName);
        }
        return false;
    }
}
