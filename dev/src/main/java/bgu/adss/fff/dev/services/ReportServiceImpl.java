package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.DefectiveItemsReportRepository;
import bgu.adss.fff.dev.data.InventoryReportRepository;
import bgu.adss.fff.dev.data.OutOfStockReportRepository;
import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.*;
import bgu.adss.fff.dev.exceptions.ReportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ReportServiceImpl implements ReportService {

    private final InventoryReportRepository inventoryReportRepository;
    private final OutOfStockReportRepository outOfStockReportRepository;
    private final DefectiveItemsReportRepository defectiveItemsReportRepository;

    private final ProductRepository productRepository;

    @Autowired
    public ReportServiceImpl(
            InventoryReportRepository inventoryReportRepository,
            OutOfStockReportRepository outOfStockReportRepository,
            DefectiveItemsReportRepository defectiveItemsReportRepository,
            ProductRepository productRepository) {
        this.inventoryReportRepository = inventoryReportRepository;
        this.outOfStockReportRepository = outOfStockReportRepository;
        this.defectiveItemsReportRepository = defectiveItemsReportRepository;
        this.productRepository = productRepository;
    }

    private long generateRandomItemID(JpaRepository<? extends Report, Long> repository) {
        long id =  new Random().nextLong();

        while (repository.existsById(id)) {
            id = new Random().nextLong();
        }

        return id;
    }

    @Override
    public Report createReport(Report report) {
        if (report == null)
            throw new ReportException("Report is null");

        if (report.getReportType() == null)
            throw new ReportException("Report type is null");

        if (report.getReportType() == ReportType.INVENTORY) {
            return createInventoryReport((InventoryReport) report);
        } else if (report.getReportType() == ReportType.OUT_OF_STOCK) {
            return createOutOfStockReport((OutOfStockReport) report);
        } else if (report.getReportType() == ReportType.DEFECTIVE_ITEMS) {
            return createDefectiveItemsReport((DefectiveItemsReport) report);
        } else {
            throw new ReportException("Invalid report type");
        }
    }

    private InventoryReport createInventoryReport(InventoryReport inventoryReport) {

        if (inventoryReport == null)
            throw new ReportException("Inventory report is null");

        if (inventoryReportRepository.existsById(inventoryReport.getReportId()))
            throw new ReportException("Inventory report already exists");

        long id = generateRandomItemID(inventoryReportRepository);
        inventoryReport.setReportId(id);

        inventoryReport.writeReport(productRepository);
        return inventoryReportRepository.save(inventoryReport);
    }

    @Override
    public InventoryReport getInventoryReport(long id) {
        return inventoryReportRepository.findById(id).orElseThrow(() -> new ReportException("Inventory report not found"));
    }

    private OutOfStockReport createOutOfStockReport(OutOfStockReport outOfStockReport) {

        if (outOfStockReport == null)
            throw new ReportException("Out of stock report is null");

        if (outOfStockReportRepository.existsById(outOfStockReport.getReportId()))
            throw new ReportException("Out of stock report already exists");

        long id = generateRandomItemID(outOfStockReportRepository);
        outOfStockReport.setReportId(id);

        outOfStockReport.writeReport(productRepository);

        return outOfStockReportRepository.save(outOfStockReport);
    }

    @Override
    public OutOfStockReport getOutOfStockReport(long id) {
        return outOfStockReportRepository.findById(id).orElseThrow(() -> new ReportException("Out of stock report not found"));
    }

    private DefectiveItemsReport createDefectiveItemsReport(DefectiveItemsReport defectiveItemsReport) {

        if (defectiveItemsReport == null)
            throw new ReportException("Defective items report is null");

        if (defectiveItemsReportRepository.existsById(defectiveItemsReport.getReportId()))
            throw new ReportException("Defective items report already exists");

        long id = generateRandomItemID(defectiveItemsReportRepository);
        defectiveItemsReport.setReportId(id);

        defectiveItemsReport.writeReport(productRepository);
        return defectiveItemsReportRepository.save(defectiveItemsReport);
    }

    @Override
    public DefectiveItemsReport getDefectiveItemsReport(long id) {
        return defectiveItemsReportRepository.findById(id).orElseThrow(() -> new ReportException("Defective items report not found"));
    }
}
