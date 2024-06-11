package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for RequestProductDto details
 * @param productID
 * @param productName
 * @param price
 * @param minimalQuantity
 */
public record RequestProductDto(
        @JsonProperty("productID") long productID,
        @JsonProperty("productName") String productName,
        @JsonProperty("price") float price,
        @JsonProperty("minimalQuantity") int minimalQuantity
) { }
