package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.DefectiveItemsReport;
import bgu.adss.fff.dev.domain.models.InventoryReport;
import bgu.adss.fff.dev.domain.models.OutOfStockReport;
import bgu.adss.fff.dev.domain.models.Report;

public interface ReportService {

    Report createReport(Report report);

    InventoryReport getInventoryReport(long id);
    OutOfStockReport getOutOfStockReport(long id);
    DefectiveItemsReport getDefectiveItemsReport(long id);

}
