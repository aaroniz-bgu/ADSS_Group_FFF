package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.InventoryReportDto;
import bgu.adss.fff.dev.domain.models.InventoryReport;
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
    public ResponseEntity<InventoryReportDto> createReport(@RequestBody InventoryReportDto request) {
        InventoryReport report = service.createInventoryReport(map(request));
        InventoryReportDto reportDto = map(report);
        return new ResponseEntity<>(reportDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryReportDto> getReport(@PathVariable long id) {
        InventoryReport report = service.getInventoryReport(id);
        InventoryReportDto reportDto = map(report);
        return ResponseEntity.ok(reportDto);
    }

}
