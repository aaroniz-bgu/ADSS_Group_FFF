package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.EmbeddedShiftId;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, EmbeddedShiftId> {

    @Query("select s from shifts s where branch.name = :branchName and s.id.date between :fromDate and :toDate")
    List<Shift> getRangeOfShiftsByBranch(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("branchName") String branchName);
    List<Shift> findShiftsByBranch(Branch branch);
}
