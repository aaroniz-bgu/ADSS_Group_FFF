package bgu.adss.fff.dev.controllers.mappers;


import bgu.adss.fff.dev.contracts.DefectiveItemsReportDto;
import bgu.adss.fff.dev.contracts.InventoryReportDto;
import bgu.adss.fff.dev.contracts.OutOfStockReportDto;
import bgu.adss.fff.dev.domain.models.DefectiveItemsReport;
import bgu.adss.fff.dev.domain.models.InventoryReport;
import bgu.adss.fff.dev.domain.models.OutOfStockReport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

}
