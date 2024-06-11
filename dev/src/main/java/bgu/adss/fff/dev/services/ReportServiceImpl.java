package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.*;
import bgu.adss.fff.dev.domain.models.*;
import bgu.adss.fff.dev.exceptions.ReportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Service
public class ReportServiceImpl implements ReportService {

    private final InventoryReportRepository inventoryReportRepository;
    private final OutOfStockReportRepository outOfStockReportRepository;
    private final DefectiveItemsReportRepository defectiveItemsReportRepository;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * ReportServiceImpl constructor
     * @param inventoryReportRepository inventory report repository
     * @param outOfStockReportRepository out of stock report repository
     * @param defectiveItemsReportRepository defective items report repository
     * @param productRepository product repository
     * @param categoryRepository category repository
     */
    @Autowired
    public ReportServiceImpl(
            InventoryReportRepository inventoryReportRepository,
            OutOfStockReportRepository outOfStockReportRepository,
            DefectiveItemsReportRepository defectiveItemsReportRepository,
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.inventoryReportRepository = inventoryReportRepository;
        this.outOfStockReportRepository = outOfStockReportRepository;
        this.defectiveItemsReportRepository = defectiveItemsReportRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // Helper methods

    private long generateRandomItemID(JpaRepository<? extends Report, Long> repository) {
        long id =  new Random().nextLong();

        while (repository.existsById(id)) {
            id = new Random().nextLong();
        }

        return id;
    }

    /**
     * Create a new report
     * @param report report to create
     * @param categories categories to include in the report
     * @return created report
     */
    @Override
    public Report createReport(Report report, String[] categories) {
        if (report == null)
            throw new ReportException("Report is null", HttpStatus.BAD_REQUEST);

        if (report.getReportType() == null)
            throw new ReportException("Report type is null", HttpStatus.BAD_REQUEST);

        if (report.getReportType() == ReportType.INVENTORY) {
            return createInventoryReport((InventoryReport) report, categories);
        } else if (report.getReportType() == ReportType.OUT_OF_STOCK) {
            return createOutOfStockReport((OutOfStockReport) report);
        } else if (report.getReportType() == ReportType.DEFECTIVE_ITEMS) {
            return createDefectiveItemsReport((DefectiveItemsReport) report);
        } else {
            throw new ReportException("Invalid report type", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Create an inventory report
     * @param inventoryReport inventory report to create
     * @param categories categories to include in the report
     * @return created inventory report
     */
    private InventoryReport createInventoryReport(InventoryReport inventoryReport, String[] categories) {

        if (inventoryReport == null)
            throw new ReportException("Inventory report is null", HttpStatus.BAD_REQUEST);

        if (inventoryReportRepository.existsById(inventoryReport.getReportId()))
            throw new ReportException("Inventory report already exists", HttpStatus.BAD_REQUEST);

        long id = generateRandomItemID(inventoryReportRepository);
        inventoryReport.setReportId(id);

        List<Category> categoryList = new LinkedList<>();
        for (String categoryName : categories) {
            Category category = categoryRepository.findById(categoryName).orElseThrow(() -> new ReportException("Category not found", HttpStatus.BAD_REQUEST));
            categoryList.add(category);
        }
        inventoryReport.setCategories(categoryList);

        inventoryReport.writeReport(productRepository);
        return inventoryReportRepository.save(inventoryReport);
    }

    /**
     * Get an inventory report by id
     * @param id inventory report id
     * @return inventory report
     */
    @Override
    public InventoryReport getInventoryReport(long id) {
        return inventoryReportRepository.findById(id).orElseThrow(() -> new ReportException("Inventory report not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Create an out of stock report
     * @param outOfStockReport out of stock report to create
     * @return created out of stock report
     */
    private OutOfStockReport createOutOfStockReport(OutOfStockReport outOfStockReport) {

        if (outOfStockReport == null)
            throw new ReportException("Out of stock report is null", HttpStatus.BAD_REQUEST);

        if (outOfStockReportRepository.existsById(outOfStockReport.getReportId()))
            throw new ReportException("Out of stock report already exists", HttpStatus.BAD_REQUEST);

        long id = generateRandomItemID(outOfStockReportRepository);
        outOfStockReport.setReportId(id);

        outOfStockReport.writeReport(productRepository);

        return outOfStockReportRepository.save(outOfStockReport);
    }

    /**
     * Get an out of stock report by id
     * @param id out of stock report id
     * @return out of stock report
     */
    @Override
    public OutOfStockReport getOutOfStockReport(long id) {
        return outOfStockReportRepository.findById(id).orElseThrow(() -> new ReportException("Out of stock report not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Create a defective items report
     * @param defectiveItemsReport defective items report to create
     * @return created defective items report
     */
    private DefectiveItemsReport createDefectiveItemsReport(DefectiveItemsReport defectiveItemsReport) {

        if (defectiveItemsReport == null)
            throw new ReportException("Defective items report is null", HttpStatus.BAD_REQUEST);

        if (defectiveItemsReportRepository.existsById(defectiveItemsReport.getReportId()))
            throw new ReportException("Defective items report already exists", HttpStatus.BAD_REQUEST);

        long id = generateRandomItemID(defectiveItemsReportRepository);
        defectiveItemsReport.setReportId(id);

        defectiveItemsReport.writeReport(productRepository);
        return defectiveItemsReportRepository.save(defectiveItemsReport);
    }

    /**
     * Get a defective items report by id
     * @param id defective items report id
     * @return defective items report
     */
    @Override
    public DefectiveItemsReport getDefectiveItemsReport(long id) {
        return defectiveItemsReportRepository.findById(id).orElseThrow(() -> new ReportException("Defective items report not found", HttpStatus.NOT_FOUND));
    }
}
