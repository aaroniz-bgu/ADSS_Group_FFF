package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DiscountDto(
        @JsonProperty("discountID") long discountID,

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("startDate") String startDate,

        @JsonProperty("numOfDays") int numOfDays,
        @JsonProperty("discountPercent") float discountPercent,
        @JsonProperty("isValid") boolean isValid
) { }
