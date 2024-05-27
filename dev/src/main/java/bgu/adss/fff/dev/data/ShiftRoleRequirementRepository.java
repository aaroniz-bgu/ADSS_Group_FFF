package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.ShiftRoleRequirement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRoleRequirementRepository extends
        JpaRepository<ShiftRoleRequirement, ShiftRoleRequirement.ShiftRoleRequirementId> {
}
