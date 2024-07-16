package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract for RequestItemDto details
 * @param expirationDate
 * @param isDefected
 * @param amount
 */
public record RequestItemDto(
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("expirationDate") String expirationDate,

        @JsonProperty("isDefected") boolean isDefected,

        @JsonProperty("amount") int amount,
        @JsonProperty("branch") String branch
) { }
