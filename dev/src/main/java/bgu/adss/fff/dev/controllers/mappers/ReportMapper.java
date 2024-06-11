package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.*;
import bgu.adss.fff.dev.domain.models.*;
import bgu.adss.fff.dev.exceptions.ReportException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    /**
     * Maps an InventoryReport to an InventoryReportDto
     * @param inventoryReport InventoryReport to be mapped to InventoryReportDto
     * @return InventoryReportDto
     */
    public static InventoryReportDto map(InventoryReport inventoryReport){
        return new InventoryReportDto(
                inventoryReport.getReportId(),
                inventoryReport.getReportDate().format(formatter),
                inventoryReport.getTitle(),
                inventoryReport.getContent(),
                CategoryMapper.map(inventoryReport.getCategories())
        );
    }

    /**
     * Maps an InventoryReportDto to an InventoryReport
     * @param inventoryReportDto InventoryReportDto to be mapped to InventoryReport
     * @return InventoryReport
     */
    public static InventoryReport map(InventoryReportDto inventoryReportDto){
        return new InventoryReport(
                inventoryReportDto.reportId(),
                LocalDateTime.parse(inventoryReportDto.reportDate(), formatter),
                inventoryReportDto.title(),
                inventoryReportDto.content(),
                CategoryMapper.map(inventoryReportDto.categories())
        );
    }

    /**
     * Maps an OutOfStockReport to an OutOfStockReportDto
     * @param outOfStockReport OutOfStockReport to be mapped to OutOfStockReportDto
     * @return OutOfStockReportDto
     */
    public static OutOfStockReportDto map(OutOfStockReport outOfStockReport){
        return new OutOfStockReportDto(
                outOfStockReport.getReportId(),
                outOfStockReport.getReportDate().format(formatter),
                outOfStockReport.getTitle(),
                outOfStockReport.getContent()
        );
    }

    /**
     * Maps an OutOfStockReportDto to an OutOfStockReport
     * @param outOfStockReportDto OutOfStockReportDto to be mapped to OutOfStockReport
     * @return OutOfStockReport
     */
    public static OutOfStockReport map(OutOfStockReportDto outOfStockReportDto){
        return new OutOfStockReport(
                outOfStockReportDto.reportId(),
                LocalDateTime.parse(outOfStockReportDto.reportDate(), formatter),
                outOfStockReportDto.title(),
                outOfStockReportDto.content()
        );
    }

    /**
     * Maps a DefectiveItemsReport to a DefectiveItemsReportDto
     * @param defectiveItemsReport DefectiveItemsReport to be mapped to DefectiveItemsReportDto
     * @return DefectiveItemsReportDto
     */
    public static DefectiveItemsReportDto map(DefectiveItemsReport defectiveItemsReport){
        return new DefectiveItemsReportDto(
                defectiveItemsReport.getReportId(),
                defectiveItemsReport.getReportDate().format(formatter),
                defectiveItemsReport.getTitle(),
                defectiveItemsReport.getContent()
        );
    }

    /**
     * Maps a DefectiveItemsReportDto to a DefectiveItemsReport
     * @param defectiveItemsReportDto DefectiveItemsReportDto to be mapped to DefectiveItemsReport
     * @return DefectiveItemsReport
     */
    public static DefectiveItemsReport map(DefectiveItemsReportDto defectiveItemsReportDto){
        return new DefectiveItemsReport(
                defectiveItemsReportDto.reportId(),
                LocalDateTime.parse(defectiveItemsReportDto.reportDate(), formatter),
                defectiveItemsReportDto.title(),
                defectiveItemsReportDto.content()
        );
    }

    /**
     * Maps a RequestReportDto to a Report
     * @param requestReportDto RequestReportDto to be mapped to Report
     * @return Report
     */
    public static Report map(RequestReportDto requestReportDto){
        if (requestReportDto.reportType() == ReportType.INVENTORY) {
            return new InventoryReport(0, LocalDateTime.now(), "Inventory Report", null);
        }
        if (requestReportDto.reportType() == ReportType.OUT_OF_STOCK) {
            return new OutOfStockReport(0, LocalDateTime.now(), "Out of Stock Report", null);
        }
        if (requestReportDto.reportType() == ReportType.DEFECTIVE_ITEMS) {
            return new DefectiveItemsReport(0, LocalDateTime.now(), "Defective Items Report", null);
        }

        throw new ReportException("Invalid report type", HttpStatus.BAD_REQUEST);
    }

    /**
     * Maps a Report to a ReportDto
     * @param report Report to be mapped to ReportDto
     * @return ReportDto
     */
    public static ReportDto map(Report report){
        return new ReportDto(
                report.getReportId(),
                report.getReportDate().format(formatter),
                report.getTitle(),
                report.getContent(),
                report.getReportType());
    }

}
