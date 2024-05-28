package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.ShiftDto;
import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.domain.models.Shift;
import bgu.adss.fff.dev.domain.models.ShiftDayPart;

import java.util.Arrays;

/**
 * The ShiftMapper class provides static methods to map shift-related internal objects to shift-related DTOs.
 * This class follows the Mapper design pattern, allowing for seamless conversion between internal data structures
 * and data transfer objects (DTOs).
 */
public class ShiftMapper {
    /**
     * Maps a Shift object to a ShiftDto object.
     *
     * @param shift the Shift object to be mapped
     * @return a ShiftDto object containing data from the provided Shift object
     */
    public static ShiftDto map(Shift shift) {
        return new ShiftDto(
                shift.getDate(),
                shift.getShiftDayPart().ordinal(),
                shift.isLocked(),
                shift.getAvailableEmployees().stream()
                        .map(EmployeeMapper::map)
                        .toArray(EmployeeDto[]::new),
                shift.getAssignedEmployees().stream()
                        .map(EmployeeMapper::map)
                        .toArray(EmployeeDto[]::new),
                shift.getRequiredRoles().stream()
                        .map(Role::getName)
                        .toArray(String[]::new)
        );
    }

    /**
     * Maps a ShiftDto object to a Shift object.
     *
     * @param dto the ShiftDto object to be mapped
     * @return a Shift object containing data from the provided ShiftDto object
     */
    public static Shift map(ShiftDto dto) {
        Shift out = new Shift(
                dto.date(),
                ShiftDayPart.values()[dto.shift()],
                dto.isLocked()
        );
        // Note that there might be duplicates between the two:
        out.getAvailableEmployees().addAll(Arrays.stream(dto.availableEmployees())
                .map(EmployeeMapper::map)
                .toList());
        out.getAssignedEmployees().addAll(Arrays.stream(dto.assignedEmployees())
                .map(EmployeeMapper::map)
                .toList()
        );
        // Should be replaced with instances from the repository:
        out.setRequiredRoles(Arrays.stream(dto.requiredRoles())
                .map((s) -> new Role(s, false))
                .toList());
        return out;
    }
}
