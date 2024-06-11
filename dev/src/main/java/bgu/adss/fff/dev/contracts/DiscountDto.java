package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Contract for Discount details
 * @param discountID
 * @param startDate
 * @param endDate
 * @param discountPercent
 */
public record DiscountDto(
        @JsonProperty("discountID") long discountID,

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("startDate") String startDate,

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("endDate") String endDate,

        @JsonProperty("discountPercent") float discountPercent
) { }
