package bgu.adss.fff.dev.contracts;

import bgu.adss.fff.dev.domain.models.Category;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contract for InventoryReportDto details
 * @param categories
 */
public record InventoryReportDto(
        @JsonProperty("categories") List<Category> categories
) { }
