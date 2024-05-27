package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Role;

import java.util.Collection;
import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    List<Role> getRoles();
    Role getRole(String name);
    void removeRole(String name);
    /**
     * Returns all the roles if and only if for all r in roles exist in the system.
     * @param roles
     * @return List of roles if all exist, null otherwise.
     */
    List<Role> returnIfExists(Collection<String> roles);
}
