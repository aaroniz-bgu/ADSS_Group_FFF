package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.Shift;
import bgu.adss.fff.dev.domain.models.ShiftDayPart;

import java.time.LocalDate;
import java.util.List;

public interface ShiftService {
    Shift getShift(LocalDate date, ShiftDayPart dayPart);
    List<Shift> getShifts(LocalDate from, LocalDate to);
    void lockShift(LocalDate date, ShiftDayPart dayPart);
    void unlockShift(LocalDate date, ShiftDayPart dayPart);

    void reportAvailability(Long empId, LocalDate date, ShiftDayPart dayPart);

    List<Employee> getAvailableEmployees(LocalDate date, ShiftDayPart dayPart);
    List<Employee> getAssignedEmployees(LocalDate date, ShiftDayPart dayPart);
    void assignEmployees(List<Employee> employees, LocalDate date, ShiftDayPart dayPart);

    void addRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean reoccurring);
    void remRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean once);
}
