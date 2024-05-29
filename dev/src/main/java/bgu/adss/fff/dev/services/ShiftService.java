package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.Shift;
import bgu.adss.fff.dev.domain.models.ShiftDayPart;

import java.time.LocalDate;
import java.util.List;

public interface ShiftService {
    Shift getShift(LocalDate date, ShiftDayPart dayPart, String branch);
    List<Shift> getShifts(LocalDate from, LocalDate to);
    List<Shift> getShiftsByBranch(LocalDate from, LocalDate to, String branch);
    void lockShift(LocalDate date, ShiftDayPart dayPart, String branch);
    void unlockShift(LocalDate date, ShiftDayPart dayPart, String branch);

    void reportAvailability(Long empId, LocalDate date, ShiftDayPart dayPart);

    List<Employee> getAvailableEmployees(LocalDate date, ShiftDayPart dayPart, String branch);
    List<Employee> getAssignedEmployees(LocalDate date, ShiftDayPart dayPart, String branch);
    void assignEmployees(List<Employee> employees, LocalDate date, ShiftDayPart dayPart, String branch);

    void addRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean reoccurring, String branch);
    void remRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean once , String branch);
}
