package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for OutOfStockReportDto details
 * @param reportId
 * @param reportDate
 * @param title
 * @param content
 */
public record OutOfStockReportDto(
        @JsonProperty("reportId") long reportId,

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("reportDate") String reportDate,

        @JsonProperty("title") String title,
        @JsonProperty("content") String content,
        @JsonProperty("content") String branch
) { }