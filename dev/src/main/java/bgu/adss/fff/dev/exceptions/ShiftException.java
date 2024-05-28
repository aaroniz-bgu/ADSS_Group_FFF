package bgu.adss.fff.dev.exceptions;

import bgu.adss.fff.dev.domain.models.Shift;
import bgu.adss.fff.dev.domain.models.ShiftDayPart;

import java.time.LocalDate;

public class ShiftException extends RuntimeException {

    private ShiftException(String msg) {
        super(msg);
    }

    public static ShiftException noShiftManger(LocalDate date, ShiftDayPart dayPart) {
        return new ShiftException("No shift manager was assigned in: " + date.toString() + "-:" + dayPart.ordinal());
    }
}
