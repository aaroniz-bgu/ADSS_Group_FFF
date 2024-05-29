package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ProductDto(
        @JsonProperty("productID") long productID,
        @JsonProperty("productName") String productName,
        @JsonProperty("price") float price,
        @JsonProperty("discount") DiscountDto discount,
        @JsonProperty("shelves") Map<Long, ItemDto> shelves,
        @JsonProperty("storage") Map<Long, ItemDto> storage,
        @JsonProperty("minimalQuantity") int minimalQuantity
) { }
