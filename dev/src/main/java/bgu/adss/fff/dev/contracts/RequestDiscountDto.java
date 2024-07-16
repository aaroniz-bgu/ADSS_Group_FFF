package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for RequestDiscountDto details
 * @param startDate
 * @param endDate
 * @param discountPercent
 */
public record RequestDiscountDto(
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("startDate") String startDate,

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("endDate") String endDate,

        @JsonProperty("discountPercent") float discountPercent
) { }
