package bgu.adss.fff.dev.contracts;

/**
 * Contract for Category details
 */

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for Report details
 * @param categories
 */
public record RequestCategoriesDto(
        @JsonProperty("categories") String[] categories
) { }
