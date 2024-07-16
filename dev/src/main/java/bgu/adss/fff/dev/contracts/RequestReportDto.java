package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.ReportType;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for Report details
 * @param reportType
 * @param categories
 */
public record RequestReportDto(
        @JsonProperty("reportType") ReportType reportType,
        @JsonProperty("categories") String[] categories
) { }
