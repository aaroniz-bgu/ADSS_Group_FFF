package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.RoleRepository;
import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.exceptions.RoleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    // TODO make tests for this.
//    @Test
//    void testRemoveRole() {
//
//    }
}