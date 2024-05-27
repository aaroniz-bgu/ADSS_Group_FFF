package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class EmbeddedShiftId implements Serializable {
    private LocalDate date;
    private ShiftDayPart shift;

    // for JPA:
    public EmbeddedShiftId() { }

    public EmbeddedShiftId(LocalDate date, ShiftDayPart shift) {
        this.date = date;
        this.shift = shift;
    }

    @Column(name = "shift_date")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Column(name = "day_part")
    public ShiftDayPart getShift() {
        return shift;
    }

    public void setShift(ShiftDayPart shift) {
        this.shift = shift;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof EmbeddedShiftId other) {
            return date.equals(other.date) && (shift == other.shift);
        }
        return false;
    }
}
