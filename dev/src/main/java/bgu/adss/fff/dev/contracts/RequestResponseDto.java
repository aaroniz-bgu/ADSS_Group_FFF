package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.ReportType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RequestResponseDto(
        @JsonProperty("reportType") ReportType reportType,
        @JsonProperty("categories") RequestCategoryDto[] categories
) { }
