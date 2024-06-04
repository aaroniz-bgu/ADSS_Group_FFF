package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.DefectiveItemsReport;
import bgu.adss.fff.dev.domain.models.InventoryReport;
import bgu.adss.fff.dev.domain.models.OutOfStockReport;

public interface ReportService {

    InventoryReport createInventoryReport(InventoryReport inventoryReport);
    InventoryReport getInventoryReport(long id);

    OutOfStockReport createOutOfStockReport(OutOfStockReport outOfStockReport);
    OutOfStockReport getOutOfStockReport(long id);

    DefectiveItemsReport createDefectiveItemsReport(DefectiveItemsReport defectiveItemsReport);
    DefectiveItemsReport getDefectiveItemsReport(long id);

}
