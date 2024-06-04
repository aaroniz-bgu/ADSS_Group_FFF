package bgu.adss.fff.dev.controllers.mappers;


import bgu.adss.fff.dev.contracts.*;
import bgu.adss.fff.dev.domain.models.*;
import bgu.adss.fff.dev.exceptions.ReportException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ReportMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy HH:mm:ss");

    public static InventoryReportDto map(InventoryReport inventoryReport){
        return new InventoryReportDto(
                inventoryReport.getReportId(),
                inventoryReport.getReportDate().format(formatter),
                inventoryReport.getTitle(),
                inventoryReport.getContent(),
                CategoryMapper.map(inventoryReport.getCategories())
        );
    }

    public static InventoryReport map(InventoryReportDto inventoryReportDto){
        return new InventoryReport(
                inventoryReportDto.reportId(),
                LocalDateTime.parse(inventoryReportDto.reportDate(), formatter),
                inventoryReportDto.title(),
                inventoryReportDto.content(),
                CategoryMapper.map(inventoryReportDto.categories())
        );
    }

    public static OutOfStockReportDto map(OutOfStockReport outOfStockReport){
        return new OutOfStockReportDto(
                outOfStockReport.getReportId(),
                outOfStockReport.getReportDate().format(formatter),
                outOfStockReport.getTitle(),
                outOfStockReport.getContent()
        );
    }

    public static OutOfStockReport map(OutOfStockReportDto outOfStockReportDto){
        return new OutOfStockReport(
                outOfStockReportDto.reportId(),
                LocalDateTime.parse(outOfStockReportDto.reportDate(), formatter),
                outOfStockReportDto.title(),
                outOfStockReportDto.content()
        );
    }

    public static DefectiveItemsReportDto map(DefectiveItemsReport defectiveItemsReport){
        return new DefectiveItemsReportDto(
                defectiveItemsReport.getReportId(),
                defectiveItemsReport.getReportDate().format(formatter),
                defectiveItemsReport.getTitle(),
                defectiveItemsReport.getContent()
        );
    }

    public static DefectiveItemsReport map(DefectiveItemsReportDto defectiveItemsReportDto){
        return new DefectiveItemsReport(
                defectiveItemsReportDto.reportId(),
                LocalDateTime.parse(defectiveItemsReportDto.reportDate(), formatter),
                defectiveItemsReportDto.title(),
                defectiveItemsReportDto.content()
        );
    }

    public static Report map(RequestResponseDto requestResponseDto){
        if (requestResponseDto.reportType() == ReportType.INVENTORY) {
            List<Category> categories = Arrays.stream(requestResponseDto.categories()).map(CategoryMapper::map).toList();
            return new InventoryReport(0, LocalDateTime.now(), "Inventory Report", null, categories);
        }
        if (requestResponseDto.reportType() == ReportType.OUT_OF_STOCK) {
            return new OutOfStockReport(0, LocalDateTime.now(), "Out of Stock Report", null);
        }
        if (requestResponseDto.reportType() == ReportType.DEFECTIVE_ITEMS) {
            return new DefectiveItemsReport(0, LocalDateTime.now(), "Defective Items Report", null);
        }

        throw new ReportException("Invalid report type");
    }

    public static ReportDto map(Report report){
        return new ReportDto(
                report.getReportId(),
                report.getReportDate().format(formatter),
                report.getTitle(),
                report.getContent(),
                report.getReportType());
    }

}
