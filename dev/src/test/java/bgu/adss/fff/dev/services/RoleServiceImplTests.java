package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.RoleRepository;
import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.exceptions.RoleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RoleServiceImplTests {

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    private Role testRole;

    @BeforeEach
    void setup() {
        testRole = new Role("Test Role", false);
    }

    @Test
    void testCreateRole() {
        when(roleRepository.save(testRole)).thenReturn(testRole);
        assertEquals(testRole, roleService.createRole(testRole));
    }

    @Test
    void testGetRole() {
        when(roleRepository.findById(testRole.getName())).thenReturn(Optional.of(testRole));
        assertEquals(testRole, roleService.getRole(testRole.getName()));
        when(roleRepository.findById("NON-EXISTENT ROLE (Good PM)")).thenReturn(Optional.empty());
        assertThrows(RoleException.class, () -> roleService.getRole("NON-EXISTENT ROLE (Good PM)"));
    }

    @Test
    void testGetRoles() {
        List<Role> roles = Arrays.asList(new Role("Test Role", false), new Role("Another Role", false));

        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> returnedRoles = roleService.getRoles();

        assertEquals(roles.size(), returnedRoles.size());
        for (Role role : returnedRoles) {
            assertTrue(roles.contains(role));
        }
    }
}


