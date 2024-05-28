package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.RoleRepository;
import bgu.adss.fff.dev.domain.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup() {
        Role role1 = new Role("Fixerupper", false);
        Role role2 = new Role("Da big boss man", true);
        Role role3 = new Role("Something or another", false);

        entityManager.persist(role1);
        entityManager.persist(role2);
        entityManager.persist(role3);
        entityManager.flush();
    }

    @Test
    public void testFindByNameIn() {
        List<Role> foundRoles = roleRepository.findByNameIn(Arrays.asList("Fixerupper", "Da big boss man"));

        assertThat(foundRoles).hasSize(2);
        assertThat(foundRoles).extracting(Role::getName).containsOnly("Fixerupper", "Da big boss man");
    }
}
