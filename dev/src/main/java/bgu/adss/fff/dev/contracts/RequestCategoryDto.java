package bgu.adss.fff.dev.contracts;

/**
 * Contract for Category details
 */

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for Report details
 * @param categoryName
 */
public record RequestCategoryDto(
        @JsonProperty("categoryName") String categoryName
) { }
