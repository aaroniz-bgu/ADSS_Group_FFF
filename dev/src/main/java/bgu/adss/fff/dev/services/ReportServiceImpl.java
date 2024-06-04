package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.DefectiveItemsReportRepository;
import bgu.adss.fff.dev.data.InventoryReportRepository;
import bgu.adss.fff.dev.data.OutOfStockReportRepository;
import bgu.adss.fff.dev.domain.models.DefectiveItemsReport;
import bgu.adss.fff.dev.domain.models.InventoryReport;
import bgu.adss.fff.dev.domain.models.OutOfStockReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    private final InventoryReportRepository inventoryReportRepository;
    private final OutOfStockReportRepository outOfStockReportRepository;
    private final DefectiveItemsReportRepository defectiveItemsReportRepository;

    @Autowired
    public ReportServiceImpl(InventoryReportRepository inventoryReportRepository, OutOfStockReportRepository outOfStockReportRepository, DefectiveItemsReportRepository defectiveItemsReportRepository) {
        this.inventoryReportRepository = inventoryReportRepository;
        this.outOfStockReportRepository = outOfStockReportRepository;
        this.defectiveItemsReportRepository = defectiveItemsReportRepository;
    }

    @Override
    public InventoryReport createInventoryReport(InventoryReport inventoryReport) {
        return null;
    }

    @Override
    public InventoryReport getInventoryReport(long id) {
        return null;
    }

    @Override
    public OutOfStockReport createOutOfStockReport(OutOfStockReport outOfStockReport) {
        return null;
    }

    @Override
    public OutOfStockReport getOutOfStockReport(long id) {
        return null;
    }

    @Override
    public DefectiveItemsReport createDefectiveItemsReport(DefectiveItemsReport defectiveItemsReport) {
        return null;
    }

    @Override
    public DefectiveItemsReport getDefectiveItemsReport(long id) {
        return null;
    }
}
