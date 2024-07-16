package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for InventoryReportDto details
 * @param reportId
 * @param reportDate
 * @param title
 * @param content
 * @param categories
 */
public record InventoryReportDto(
        @JsonProperty("reportId") long reportId,

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("reportDate") String reportDate,

        @JsonProperty("title") String title,
        @JsonProperty("content") String content,
        @JsonProperty("categories") CategoryDto[] categories,

        @JsonProperty("branch") String branch
) { }
