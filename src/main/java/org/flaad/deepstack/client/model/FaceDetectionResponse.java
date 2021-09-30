package org.flaad.deepstack.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonDeserialize
public class FaceDetectionResponse {

    boolean success;
    int duration;
    Predictions[] predictions;

    // optional output
    String error;

    @Getter
    @ToString
    @NoArgsConstructor
    @JsonDeserialize
    public static class Predictions {
        double confidence;
        @JsonProperty("y_min")
        int yMin;
        @JsonProperty("x_min")
        int xMin;
        @JsonProperty("y_max")
        int yMax;
        @JsonProperty("x_max")
        int xMax;
    }
}
