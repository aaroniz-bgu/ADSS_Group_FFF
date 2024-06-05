package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductDto(
        @JsonProperty("productID") long productID,
        @JsonProperty("productName") String productName,
        @JsonProperty("price") float price,
        @JsonProperty("discount") DiscountDto discount,
        @JsonProperty("shelves") ItemDto[] shelves,
        @JsonProperty("storage") ItemDto[] storage,
        @JsonProperty("minimalQuantity") int minimalQuantity
) { }
