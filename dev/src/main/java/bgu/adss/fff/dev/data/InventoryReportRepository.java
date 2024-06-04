package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.InventoryReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryReportRepository extends JpaRepository<InventoryReport, Long> { }
