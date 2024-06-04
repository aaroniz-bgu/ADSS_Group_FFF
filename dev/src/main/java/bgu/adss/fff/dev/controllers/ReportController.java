package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.DefectiveItemsReportDto;
import bgu.adss.fff.dev.contracts.InventoryReportDto;
import bgu.adss.fff.dev.contracts.OutOfStockReportDto;
import bgu.adss.fff.dev.domain.models.DefectiveItemsReport;
import bgu.adss.fff.dev.domain.models.InventoryReport;
import bgu.adss.fff.dev.domain.models.OutOfStockReport;
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

    @PostMapping
    public ResponseEntity<InventoryReportDto> createInventoryReport(@RequestBody InventoryReportDto request) {
        InventoryReport report = service.createInventoryReport(map(request));
        InventoryReportDto reportDto = map(report);
        return new ResponseEntity<>(reportDto, HttpStatus.CREATED);
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<InventoryReportDto> getInventoryReport(@PathVariable long id) {
        InventoryReport report = service.getInventoryReport(id);
        InventoryReportDto reportDto = map(report);
        return ResponseEntity.ok(reportDto);
    }

    @PostMapping
    public ResponseEntity<OutOfStockReportDto> createOutOfStockReport(@RequestBody OutOfStockReportDto request) {
        OutOfStockReport report = service.createOutOfStockReport(map(request));
        OutOfStockReportDto reportDto = map(report);
        return new ResponseEntity<>(reportDto, HttpStatus.CREATED);
    }

    @GetMapping("/stock/{id}")
    public ResponseEntity<OutOfStockReportDto> getOutOfStockReport(@PathVariable long id) {
        OutOfStockReport report = service.getOutOfStockReport(id);
        OutOfStockReportDto reportDto = map(report);
        return ResponseEntity.ok(reportDto);
    }

    @PostMapping
    public ResponseEntity<DefectiveItemsReportDto> createDefectiveItemsReport(@RequestBody DefectiveItemsReportDto request) {
        DefectiveItemsReport report = service.createDefectiveItemsReport(map(request));
        DefectiveItemsReportDto reportDto = map(report);
        return new ResponseEntity<>(reportDto, HttpStatus.CREATED);
    }

    @GetMapping("/defective/{id}")
    public ResponseEntity<DefectiveItemsReportDto> getDefectiveItemsReport(@PathVariable long id) {
        DefectiveItemsReport report = service.getDefectiveItemsReport(id);
        DefectiveItemsReportDto reportDto = map(report);
        return ResponseEntity.ok(reportDto);
    }

}
