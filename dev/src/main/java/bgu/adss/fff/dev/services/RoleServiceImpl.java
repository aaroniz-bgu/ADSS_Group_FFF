package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.RoleRepository;
import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.exceptions.RoleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) { this.repository = repository; }

    /**
     * Creates a new role in the system.
     * @param role
     * @return The created role.
     */
    @Override
    public Role createRole(Role role) {
        if(repository.existsById(role.getName())) {
            throw RoleException.alreadyExists(role.getName());
        }
        return repository.save(role);
    }

    /**
     * Gets all roles in the system.
     * @return A list of all roles in the system.
     */
    @Override
    public List<Role> getRoles() { return repository.findAll(); }

    /**
     * Gets a role by its name.
     * @param name
     * @return The role with the given name.
     */
    @Override
    public Role getRole(String name) {
        return repository.findById(name).orElseThrow(() -> RoleException.notFound(name));
    }

    /**
     * Removes a role by its name.
     * @param name
     */
    @Override
    public void removeRole(String name) {
        if(!repository.existsById(name)) {
            throw RoleException.notFound(name);
        }
        repository.deleteById(name);
    }
}