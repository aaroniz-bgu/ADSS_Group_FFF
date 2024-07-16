package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.services.RoleService;

import java.util.List;

public class RoleStarter {

    private final Role[] roles = {
            new Role("shift-manager", true, false),
            new Role("storekeeper", false, false),
            new Role("hr-manager", false, true),
            new Role("cashier", false, false),
            new Role("driver", false, false)
    };

    private final RoleService service;

    public RoleStarter(RoleService service) {
        this.service = service;
    }

    public void loadRoles() {
        List<Role> existingRoles = service.getRoles();
        for(Role r : roles) {
            if(!containsRole(existingRoles, r)) {
                service.createRole(r);
            }
        }
    }

    private boolean containsRole(List<Role> list, Role role) {
        for (Role r : list) {
            if(r.getName().equals(role.getName())) {
                return true;
            }
        }
        return false;
    }
}
