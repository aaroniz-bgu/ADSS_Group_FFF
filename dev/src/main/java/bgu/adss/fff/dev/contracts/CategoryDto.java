package bgu.adss.fff.dev.contracts;

/**
 * Contract for Category details
 */

import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.domain.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Contract for Report details
 * @param categoryID
 * @param categoryName
 * @param children
 * @param products
 */
public record CategoryDto(
        @JsonProperty("categoryID") long categoryID,
        @JsonProperty("categoryName") String categoryName,
        @JsonProperty("children") Map<String, Category> children,
        @JsonProperty("products") Map<String, Product> products
) { }
