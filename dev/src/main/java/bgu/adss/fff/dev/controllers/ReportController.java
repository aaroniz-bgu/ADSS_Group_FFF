package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.*;
import bgu.adss.fff.dev.domain.models.DefectiveItemsReport;
import bgu.adss.fff.dev.domain.models.InventoryReport;
import bgu.adss.fff.dev.domain.models.OutOfStockReport;
import bgu.adss.fff.dev.domain.models.Report;
import bgu.adss.fff.dev.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static bgu.adss.fff.dev.controllers.mappers.ReportMapper.map;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService service;

    @Autowired
    public ReportController(ReportService service) {
        this.service = service;
    }

    // Basic CRUD operations

    /**
     * Establishes a new report in the system.
     * @param request The request containing the requested report:<br>
     *                - {@code ReportType reportType}: The type of the report to be created.<br>
     *                - {@code String[] categories}: The categories to be included in the report.
     * @return ResponseEntity containing the created report if successful, or an error message if not.
     */
    @PostMapping
    public ResponseEntity<ReportDto> createReport(@RequestBody RequestReportDto request) {
        Report report = service.createReport(map(request), request.categories());
        ReportDto reportDto = map(report);
        return new ResponseEntity<>(reportDto, HttpStatus.CREATED);
    }

    /**
     * Fetches an inventory report by its unique identifier.
     * @param id The inventory report's unique identifier.
     * @return ResponseEntity containing the report if found, or an error message if not.
     */
    @GetMapping("/inventory/{id}")
    public ResponseEntity<InventoryReportDto> getInventoryReport(@PathVariable long id) {
        InventoryReport report = service.getInventoryReport(id);
        InventoryReportDto reportDto = map(report);
        return ResponseEntity.ok(reportDto);
    }

    /**
     * Fetches an out-of-stock report by its unique identifier.
     * @param id The out-of-stock report's unique identifier.
     * @return ResponseEntity containing the report if found, or an error message if not.
     */
    @GetMapping("/stock/{id}")
    public ResponseEntity<OutOfStockReportDto> getOutOfStockReport(@PathVariable long id) {
        OutOfStockReport report = service.getOutOfStockReport(id);
        OutOfStockReportDto reportDto = map(report);
        return ResponseEntity.ok(reportDto);
    }

    /**
     * Fetches a defective items report by its unique identifier.
     * @param id The defective items report's unique identifier.
     * @return ResponseEntity containing the report if found, or an error message if not.
     */
    @GetMapping("/defective/{id}")
    public ResponseEntity<DefectiveItemsReportDto> getDefectiveItemsReport(@PathVariable long id) {
        DefectiveItemsReport report = service.getDefectiveItemsReport(id);
        DefectiveItemsReportDto reportDto = map(report);
        return ResponseEntity.ok(reportDto);
    }

}
