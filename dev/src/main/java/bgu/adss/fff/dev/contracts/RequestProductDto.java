package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RequestProductDto(
        @JsonProperty("productID") long productID,
        @JsonProperty("productName") String productName,
        @JsonProperty("price") float price,
        @JsonProperty("minimalQuantity") int minimalQuantity
) { }
