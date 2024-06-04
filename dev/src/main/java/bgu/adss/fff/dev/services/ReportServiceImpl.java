package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.DefectiveItemsReportRepository;
import bgu.adss.fff.dev.data.InventoryReportRepository;
import bgu.adss.fff.dev.data.OutOfStockReportRepository;
import bgu.adss.fff.dev.domain.models.DefectiveItemsReport;
import bgu.adss.fff.dev.domain.models.InventoryReport;
import bgu.adss.fff.dev.domain.models.OutOfStockReport;
import bgu.adss.fff.dev.exceptions.ReportException;
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

        if (inventoryReport == null)
            throw new ReportException("Inventory report is null");

        if (inventoryReportRepository.existsById(inventoryReport.getReportId()))
            throw new ReportException("Inventory report already exists");

        inventoryReport.writeReport();
        return inventoryReportRepository.save(inventoryReport);
    }

    @Override
    public InventoryReport getInventoryReport(long id) {
        return inventoryReportRepository.findById(id).orElseThrow(() -> new ReportException("Inventory report not found"));
    }

    @Override
    public OutOfStockReport createOutOfStockReport(OutOfStockReport outOfStockReport) {

        if (outOfStockReport == null)
            throw new ReportException("Out of stock report is null");

        if (outOfStockReportRepository.existsById(outOfStockReport.getReportId()))
            throw new ReportException("Out of stock report already exists");

        outOfStockReport.writeReport();
        return outOfStockReportRepository.save(outOfStockReport);
    }

    @Override
    public OutOfStockReport getOutOfStockReport(long id) {
        return outOfStockReportRepository.findById(id).orElseThrow(() -> new ReportException("Out of stock report not found"));
    }

    @Override
    public DefectiveItemsReport createDefectiveItemsReport(DefectiveItemsReport defectiveItemsReport) {

        if (defectiveItemsReport == null)
            throw new ReportException("Defective items report is null");

        if (defectiveItemsReportRepository.existsById(defectiveItemsReport.getReportId()))
            throw new ReportException("Defective items report already exists");

        defectiveItemsReport.writeReport();
        return defectiveItemsReportRepository.save(defectiveItemsReport);
    }

    @Override
    public DefectiveItemsReport getDefectiveItemsReport(long id) {
        return defectiveItemsReportRepository.findById(id).orElseThrow(() -> new ReportException("Defective items report not found"));
    }
}
