package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
public class EmbeddedShiftId implements Serializable {
    private LocalDateTime date;
    private ShiftDayPart shift;

    public EmbeddedShiftId(LocalDateTime date, ShiftDayPart shift) {
        this.date = date;
        this.shift = shift;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

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
