package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.InventoryReport;
import bgu.adss.fff.dev.domain.models.OutOfStockReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutOfStockReportRepository extends JpaRepository<OutOfStockReport, Long> { }
