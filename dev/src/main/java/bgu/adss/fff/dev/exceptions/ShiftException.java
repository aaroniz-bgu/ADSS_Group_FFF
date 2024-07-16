package bgu.adss.fff.dev.exceptions;

import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public class ShiftException extends AppException {

    private ShiftException(String msg, HttpStatus status) {
        super(msg, status);
    }

    public static ShiftException noShiftManger(LocalDate date, ShiftDayPart dayPart) {
        return new ShiftException("No shift manager was assigned in: " + date.toString() + "-:" + dayPart.ordinal(),
                HttpStatus.BAD_REQUEST);
    }
    public static ShiftException illegalAssignment(String branchName, String employeeName) {
        return new ShiftException("Illegal assignment of employee: " + employeeName + " to branch: " + branchName,
                HttpStatus.BAD_REQUEST);
    }
    public static ShiftException employeeNotAvailable(String employeeName, LocalDate date, ShiftDayPart dayPart) {
        return new ShiftException("Employee: " + employeeName + " is not available on: " + date.toString() + "-:" + dayPart.ordinal(),
                HttpStatus.BAD_REQUEST);
    }

    public static ShiftException locked(LocalDate date) {
        return new ShiftException("Cannot update shift availability at " + date.toString()
                + " since the report period is already over.", HttpStatus.LOCKED);
    }
}
