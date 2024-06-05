package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RequestAmountDto (
        @JsonProperty("amount") int amount
) { }
