package bgu.adss.fff.dev.contracts;

/**
 * Contract for Category details
 */

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for Report details
 * @param categoryName
 * @param level
 * @param children
 * @param products
 */
public record CategoryDto(
        @JsonProperty("categoryName") String categoryName,
        @JsonProperty("level") int level,
        @JsonProperty("children") CategoryDto[] children,
        @JsonProperty("products") ProductDto[] products
) { }
