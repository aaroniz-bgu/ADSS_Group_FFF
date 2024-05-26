package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.domain.models.Role;

/**
 * Maps roles to DTOs (Data Transfer Objects) and vice versa.
 */
public class RoleMapper {
    /**
     * Converts a RoleDto object to a Role entity.
     *
     * @param dto the RoleDto object to be converted
     * @return the converted Role entity
     */
    public static Role map(RoleDto dto) {
        return new Role(dto.name(), dto.isShiftManager());
    }

    /**
     * Converts a Role entity to a RoleDto object.
     *
     * @param role the Role entity to be converted
     * @return the converted RoleDto object
     */
    public static RoleDto map(Role role) {
        return new RoleDto(role.getName(), role.isShiftManager());
    }
}
