package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for RequestAmountDto details
 * @param amount
 */
public record RequestAmountDto (
        @JsonProperty("amount") int amount
) { }
