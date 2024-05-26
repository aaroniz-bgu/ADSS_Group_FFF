package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Role;
import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    List<Role> getRoles();
    Role getRole(String name);
    Role updateRole(String name, Role role);
    void removeRole(String name);
}
