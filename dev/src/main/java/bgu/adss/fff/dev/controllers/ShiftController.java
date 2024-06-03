package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.ReportShiftRequest;
import bgu.adss.fff.dev.contracts.RequireRoleRequest;
import bgu.adss.fff.dev.contracts.ShiftDto;
import bgu.adss.fff.dev.controllers.mappers.EmployeeMapper;
import bgu.adss.fff.dev.controllers.mappers.ShiftMapper;
import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Shift;
import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import bgu.adss.fff.dev.services.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static bgu.adss.fff.dev.controllers.mappers.ShiftMapper.map;

@RestController
@RequestMapping("/shift")
public class ShiftController {

    private final ShiftService service;

    @Autowired
    public ShiftController(ShiftService service) { this.service = service; }

    /**
     * Retrieves the shift that occurs/ occurred at the given date at the specified branch.
     *
     * @param date the date at which the shift occurs.
     * @param shift the {@link ShiftDayPart} ordinal.
     * @param branchName the branch name associated with this shift.
     * @return the shift details that occurs/ occurred at the given date at the specified branch.
     */
    @GetMapping("/{date}&{shift}&{branch}")
    public ResponseEntity<ShiftDto> getShift(
            @PathVariable("date") LocalDate date,
            @PathVariable("shift") int shift,
            @PathVariable("branch") String branchName
            ) {
        ShiftDayPart part = ShiftDayPart.values()[shift];
        return ResponseEntity.ok(map(service.getShift(date, part, new Branch(branchName))));
    }

    /**
     * Gets the shifts between the specified time period. Optionally if the branch name is provided,
     * it will only return the shifts that occurred in that branch.
     *
     * @param from the date from which to retrieve shifts
     * @param to the date to which retrieve shifts until (inclusive)
     * @param branchName the branch name which is associated with the desired shifts or null if needed all the
     *                   shifts of the company
     * @return The shifts detail between the specified dates. If no branch name is provided,
     * it will return all shifts. Otherwise, only the ones that occurred in that branch.
     */
    @GetMapping("{from}&{to}&{branch}")
    public ResponseEntity<ShiftDto[]> getShifts(
            @PathVariable("from") LocalDate from,
            @PathVariable("to") LocalDate to,
            @PathVariable("branch") String branchName
    ) {
        List<Shift> shifts;
        if (branchName != null && !branchName.isEmpty()) {
            shifts = service.getShiftsByBranch(from, to, new Branch(branchName));
        } else {
            shifts = service.getShifts(from, to);
        }
        return ResponseEntity.ok(shifts.stream()
                .map(ShiftMapper::map)
                .toList().toArray(ShiftDto[]::new));
    }

    /**
     * Lock/ unlocks the shift to employee reports, so no more changes could be applied to them.
     *
     * @param lock a boolean determines whether this shift supposed to be locked or unlocked.
     * @param request the request which contains the date,
     *               shift day part ordinal to identifying the shift and the branch.
     * @return ok http response if everything goes to plan.
     */
    @PutMapping("/{lock}")
    public ResponseEntity<?> lockShift(@PathVariable("lock") boolean lock, @RequestBody ShiftDto request) {
        ShiftDayPart part = ShiftDayPart.values()[request.shift()];
        if (lock) {
            service.lockShift(request.date(), part, new Branch(request.branch()));
        } else {
            service.unlockShift(request.date(), part, new Branch(request.branch()));
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
     * @param date the date at which the shift occurs.
     * @param shift the {@link ShiftDayPart} ordinal.
     * @param branchName the branch name associated with this shift.
     * @return An array of employee dtos which correspond to the available ones.
     */
    @GetMapping("/available/{date}&{shift}&{branch}")
    public ResponseEntity<EmployeeDto[]> getAvailable(
            @PathVariable("date") LocalDate date,
            @PathVariable("shift") int shift,
            @PathVariable("branch") String branchName
    ) {
        ShiftDayPart part = ShiftDayPart.values()[shift];
        return ResponseEntity.ok(service.getAvailableEmployees(
                date, part, new Branch(branchName)).stream()
                .map(EmployeeMapper::map).toList()
                .toArray(EmployeeDto[]::new));
    }

    /**
     * Retrieves the employees which were assigned to the specified shift.
     *
     * @param date the date at which the shift occurs.
     * @param shift the {@link ShiftDayPart} ordinal.
     * @param branchName the branch name associated with this shift.
     * @return An array of employee dtos which correspond to the ones which are assigned to work in it.
     */
    @GetMapping("/assigned/{date}&{shift}&{branch}")
    public ResponseEntity<EmployeeDto[]> getAssigned(
            @PathVariable("date") LocalDate date,
            @PathVariable("shift") int shift,
            @PathVariable("branch") String branchName
    ) {
        ShiftDayPart part = ShiftDayPart.values()[shift];
        return ResponseEntity.ok(service.getAssignedEmployees(
                date, part, new Branch(branchName)).stream()
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
        service.assignEmployees(
                shift.getAssignedEmployees(),
                shift.getDate(),
                shift.getShiftDayPart(),
                new Branch(shift.getBranchName()));
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
        service.addRequiredRole(
                request.role(),
                request.date(),
                part,
                request.reoccurring(),
                new Branch(request.branch()));
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
        service.remRequiredRole(
                request.role(),
                request.date(),
                part,
                request.reoccurring(),
                new Branch(request.branch()));
        return ResponseEntity.ok().build();
    }

}
