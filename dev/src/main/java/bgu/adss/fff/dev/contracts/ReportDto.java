package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.ReportType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for Report details
 * @param reportId
 * @param reportDate
 * @param title
 * @param content
 */
public record ReportDto(
        @JsonProperty("reportId") long reportId,

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("reportDate") String reportDate,

        @JsonProperty("title") String title,
        @JsonProperty("content") String content,
        @JsonProperty("reportType")ReportType reportType
) { }
