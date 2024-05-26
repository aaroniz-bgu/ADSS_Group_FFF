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

    @Override
    public Role createRole(Role role) {
        if(repository.existsById(role.getName())) {
            throw RoleException.alreadyExists(role.getName());
        }
        return repository.save(role);
    }

    @Override
    public List<Role> getRoles() { return repository.findAll(); }

    @Override
    public Role getRole(String name) {
        return repository.findById(name).orElseThrow(() -> RoleException.notFound(name));
    }

    /*
    TODO I'm worried that this method will cause problems, I don't know how to use SPRING well enough.
    Is it ok to update here? Or does the employee service need to handle this? Or does the repository
    save method call this method somehow?
     */
    @Override
    public Role updateRole(String name, Role role) {
        if(name == null || role == null) {
            throw RoleException.illegalField(name, "Role",
                    "the name and role must be set and not be null.");
        }
        if (!repository.existsById(name)) {
            throw RoleException.notFound(name);
        }
        if (!name.equals(role.getName())) {
            throw RoleException.illegalField(name, "Role", "the names must match.");
        }

        Role toUpdate = getRole(name);
        toUpdate.setShiftManager(role.isShiftManager());
        return repository.save(toUpdate);
    }

    @Override
    public void removeRole(String name) {
        if(!repository.existsById(name)) {
            throw RoleException.notFound(name);
        }
        repository.deleteById(name);
    }
}