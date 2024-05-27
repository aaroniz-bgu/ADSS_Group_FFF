package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.*;
import bgu.adss.fff.dev.controllers.mappers.EmployeeMapper;
import bgu.adss.fff.dev.controllers.mappers.ShiftMapper;
import bgu.adss.fff.dev.domain.models.Shift;
import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import bgu.adss.fff.dev.services.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static bgu.adss.fff.dev.controllers.mappers.ShiftMapper.map;

@RestController
@RequestMapping("/shift")
public class ShiftController {

    private ShiftService service;

    @Autowired
    public ShiftController(ShiftService service) { this.service = service; }

    /**
     * Retrieves the shift that occurs/ occurred at the given date.
     *
     * @param request ShiftDto object which must only contain the date & day-part enum ordinal.
     * @return the shift details that occurs/ occurred at the given date.
     */
    @GetMapping("/single")
    public ResponseEntity<ShiftDto> getShift(@RequestBody ShiftDto request) {
        ShiftDayPart part = ShiftDayPart.values()[request.shift()];
        return ResponseEntity.ok(map(service.getShift(request.date(), part)));
    }

    /**
     * Gets the shift between the specified time period.
     *
     * @param request GetShiftsRequest, 2 local dates from < to.
     * @return the shifts detail between the specified dates.
     */
    @GetMapping
    public ResponseEntity<ShiftDto[]> getShifts(@RequestBody GetShiftsRequest request) {
        return ResponseEntity.ok(service.getShifts(request.from(), request.to()).stream()
                .map(ShiftMapper::map)
                .toList().toArray(ShiftDto[]::new));
    }

    /**
     * Lock/ unlocks the shift to employee reports, so no more changes could be applied to them.
     *
     * @param lock a boolean determines whether this shift supposed to be locked or unlocked.
     * @param request the request which contains the date and shift day part ordinal to identify the shift.
     * @return ok http response if everything goes to plan.
     */
    @PutMapping("/{lock}")
    public ResponseEntity<?> lockShift(@PathVariable("lock") boolean lock, @RequestBody ShiftDto request) {
        ShiftDayPart part = ShiftDayPart.values()[request.shift()];
        if (lock) {
            service.lockShift(request.date(), part);
        } else {
            service.unlockShift(request.date(), part);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Allows an employee to report availability to a specified shift.
     *
     * @param request the report availability request.
     * @return ok http response if everything goes to plan.
     */
    @PostMapping
    public ResponseEntity<?> reportShift(@RequestBody ReportShiftRequest request) {
        ShiftDayPart part = ShiftDayPart.values()[request.shift()];
        service.reportAvailability(request.empId(), request.date(), part);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves the available employees which reported availability to the system for the specified shift.
     *
     * @param request the shift dto object which contains the shift identifiers (date & day part ordinal).
     * @return An array of employee dtos which correspond to the available ones.
     */
    @GetMapping("/available")
    public ResponseEntity<EmployeeDto[]> getAvailable(@RequestBody ShiftDto request) {
        ShiftDayPart part = ShiftDayPart.values()[request.shift()];
        return ResponseEntity.ok(service.getAvailableEmployees(request.date(), part).stream()
                .map(EmployeeMapper::map).toList()
                .toArray(EmployeeDto[]::new));
    }

    /**
     * Retrieves the employees which were assigned to the specified shift.
     *
     * @param request the shift dto object which contains the shift identifiers (day & day part ordinal).
     * @return An array of employee dtos which correspond to the ones which are assigned to work in it.
     */
    @GetMapping("/assigned")
    public ResponseEntity<EmployeeDto[]> getAssigned(@RequestBody ShiftDto request) {
        ShiftDayPart part = ShiftDayPart.values()[request.shift()];
        return ResponseEntity.ok(service.getAssignedEmployees(request.date(), part).stream()
                .map(EmployeeMapper::map).toList()
                .toArray(EmployeeDto[]::new));
    }

    /**
     * Assigns a list of employees to the shift specified in the request.
     * <br>Note! that this overrides any other assigned employees.
     *
     *
     * @param request the shift dto object which contains the shift identifiers and the assigned employee list.
     * @return ok http response if everything goes to plan.
     */
    @PutMapping("/assign")
    public ResponseEntity<?> assignEmployees(@RequestBody ShiftDto request) {
        Shift shift = map(request);
        service.assignEmployees(shift.getAssignedEmployees(), shift.getDate(), shift.getShiftDayPart());
        return ResponseEntity.ok().build();
    }

    /**
     * Adds a required role to the shift specified in the request.
     *
     * @param request must contain a string of an existing role and whether
     *                the requirement is date specific or reoccurring.
     * @return ok http response if everything goes to plan.
     */
    @PutMapping("/roles=true")
    public ResponseEntity<?> requireRole(@RequestBody RequireRoleRequest request) {
        ShiftDayPart part = ShiftDayPart.values()[request.shift()];
        service.addRequiredRole(request.role(), request.date(), part, request.reoccurring());
        return ResponseEntity.ok().build();
    }

    /**
     * Removes a required role in the shift specified in the request.
     *
     * @param request must contain a string of existing role in the shift requirements, and whether to
     *                delete it once or all of its reoccurrences in the corresponding week day to the date.
     * @return ok http response if everything goes to plan.
     */
    @PutMapping("/roles=false")
    public ResponseEntity<?> removeRequiredRole(@RequestBody RequireRoleRequest request) {
        ShiftDayPart part = ShiftDayPart.values()[request.shift()];
        service.remRequiredRole(request.role(), request.date(), part, request.reoccurring());
        return ResponseEntity.ok().build();
    }

}
