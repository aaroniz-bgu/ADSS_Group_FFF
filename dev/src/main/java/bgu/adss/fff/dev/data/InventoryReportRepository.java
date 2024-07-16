package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.InventoryReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryReportRepository extends JpaRepository<InventoryReport, Long> {
    List<InventoryReport> findInventoryReportByBranch(Branch branch);
}
