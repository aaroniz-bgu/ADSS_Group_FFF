package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record ProductDto(
        @JsonProperty("productID") long productID,
        @JsonProperty("productName") String productName,
        @JsonProperty("price") float price,
        @JsonProperty("discount") DiscountDto discount,
        @JsonProperty("shelves") List<ItemDto> shelves,
        @JsonProperty("storage") List<ItemDto> storage,
        @JsonProperty("minimalQuantity") int minimalQuantity
) { }
