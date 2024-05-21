package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.domain.models.Role;

public class RoleMapper {
    public static Role map(RoleDto dto) {
        return new Role(dto.name(), dto.isShiftManager());
    }

    public static RoleDto map(Role role) {
        return new RoleDto(role.getName(), role.isShiftManager());
    }
}
