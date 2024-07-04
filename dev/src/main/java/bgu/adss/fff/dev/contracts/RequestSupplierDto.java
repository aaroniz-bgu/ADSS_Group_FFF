package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for RequestAmountDto details
 * @param id
 * @param price
 */
public record RequestSupplierDto(
        @JsonProperty("supplierID") long id,
        @JsonProperty("supplierPrice") float price
) { }
