package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.Shift;
import bgu.adss.fff.dev.domain.models.ShiftDayPart;

import java.time.LocalDate;
import java.util.List;

public interface ShiftService {
    Shift getShift(LocalDate date, ShiftDayPart dayPart, Branch branch);
    List<Shift> getShifts(LocalDate from, LocalDate to);
    List<Shift> getShiftsByBranch(LocalDate from, LocalDate to, Branch branch);
    void lockShift(LocalDate date, ShiftDayPart dayPart, Branch branch);
    void unlockShift(LocalDate date, ShiftDayPart dayPart, Branch branch);

    void reportAvailability(Long empId, LocalDate date, ShiftDayPart dayPart);

    List<Employee> getAvailableEmployees(LocalDate date, ShiftDayPart dayPart, Branch branch);
    List<Employee> getAssignedEmployees(LocalDate date, ShiftDayPart dayPart, Branch branch);
    void assignEmployees(List<Employee> employees, LocalDate date, ShiftDayPart dayPart, Branch branch);

    void addRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean reoccurring, Branch branch);
    void remRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean once , Branch branch);
    void updateCutOff(int cutOff);
}
