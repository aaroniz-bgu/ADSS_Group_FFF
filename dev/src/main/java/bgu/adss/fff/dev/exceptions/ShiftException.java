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
    public static ShiftException illegalAssignment(String branchName, String employeeName) {
        return new ShiftException("Illegal assignment of employee: " + employeeName + " to branch: " + branchName);
    }

    public static ShiftException locked(LocalDate date) {
        return new ShiftException("Cannot update shift availability at " + date.toString()
                + " since the report period is already over."/*, HttpStatus.LOCKED*/);
    }
}
