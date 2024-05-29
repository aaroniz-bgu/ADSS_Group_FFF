package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Contract for Report details
 * @param reportId
 * @param reportDate
 * @param title
 * @param content
 */
public record ReportDto(
        @JsonProperty("reportId") long reportId,
        @JsonProperty("reportDate") LocalDateTime reportDate,
        @JsonProperty("title") String title,
        @JsonProperty("content") String content
) { }
