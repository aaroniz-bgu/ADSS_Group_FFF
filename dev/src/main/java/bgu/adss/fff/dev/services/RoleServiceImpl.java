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

    @Override
    public void removeRole(String name) {
        if(!repository.existsById(name)) {
            throw RoleException.notFound(name);
        }
        repository.deleteById(name);
    }
}