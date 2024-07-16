package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.BranchRepository;
import bgu.adss.fff.dev.data.RoleRepository;
import bgu.adss.fff.dev.data.ShiftRoleRequirementRepository;
import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import bgu.adss.fff.dev.domain.models.ShiftRoleRequirement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.DayOfWeek;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ShiftRoleRequirementRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShiftRoleRequirementRepository repository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    public void testFindByIdWeekDayAndIdPartAndIdBranchName() {
        Branch branch = new Branch("Branch1");
        entityManager.persist(branch);
        Branch branch2 = new Branch("Branch2");
        entityManager.persist(branch2);

        Role role = new Role("Role1", false, false);
        entityManager.persist(role);

        ShiftRoleRequirement shiftRoleRequirement = new ShiftRoleRequirement(DayOfWeek.MONDAY, ShiftDayPart.MORNING, role, branch);

        entityManager.persist(shiftRoleRequirement);

        ShiftRoleRequirement shiftRoleRequirement2 = new ShiftRoleRequirement(DayOfWeek.MONDAY, ShiftDayPart.MORNING, role, branch2);

        entityManager.persist(shiftRoleRequirement2);

        entityManager.flush();

        // when
        List<ShiftRoleRequirement> found = repository.findByIdWeekDayAndIdPartAndIdBranchName(DayOfWeek.MONDAY, ShiftDayPart.MORNING, "Branch1");

        // then
        assertThat(found).isNotEmpty();
        assertThat(found.get(0)).isEqualTo(shiftRoleRequirement);
        assertThat(found.get(0).getBranch().getName()).isEqualTo("Branch1");
        assertEquals(found.size(), 1);
    }
}
