package bgu.adss.fff.dev.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemDto(
        @JsonProperty("itemID") long itemID,

        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("expirationDate") String expirationDate,

        @JsonProperty("isDefected") boolean isDefected
) { }
