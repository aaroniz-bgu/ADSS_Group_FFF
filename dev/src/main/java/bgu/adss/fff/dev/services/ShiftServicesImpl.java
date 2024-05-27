package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.Shift;
import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShiftServicesImpl implements ShiftService {
    @Override
    public Shift getShift(LocalDate date, ShiftDayPart dayPart) {
        return null;
    }

    @Override
    public List<Shift> getShifts(LocalDate from, LocalDate to) {
        return null;
    }

    @Override
    public void lockShift(LocalDate date, ShiftDayPart dayPart) {

    }

    @Override
    public void unlockShift(LocalDate date, ShiftDayPart dayPart) {

    }

    @Override
    public void reportAvailability(Long empId, LocalDate date, ShiftDayPart dayPart) {

    }

    @Override
    public List<Employee> getAvailableEmployees(LocalDate date, ShiftDayPart dayPart) {
        return null;
    }

    @Override
    public List<Employee> getAssignedEmployees(LocalDate date, ShiftDayPart dayPart) {
        return null;
    }

    @Override
    public void assignEmployees(List<Employee> employees, LocalDate date, ShiftDayPart dayPart) {

    }

    @Override
    public void addRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean reoccurring) {

    }

    @Override
    public void remRequiredRole(String role, LocalDate date, ShiftDayPart dayPart, boolean once) {

    }
}
