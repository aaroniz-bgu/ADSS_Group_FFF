package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import bgu.adss.fff.dev.domain.models.ShiftRoleRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface ShiftRoleRequirementRepository extends
        JpaRepository<ShiftRoleRequirement, ShiftRoleRequirement.ShiftRoleRequirementId> {

    //List<ShiftRoleRequirement> findByIdWeekDayAndIdPart(DayOfWeek weekDay, ShiftDayPart part);
    List<ShiftRoleRequirement> findByIdWeekDayAndIdPartAndIdBranchName(DayOfWeek weekDay, ShiftDayPart part, String branchName);
}
