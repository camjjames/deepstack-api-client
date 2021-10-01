package org.flaad.deepstack.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonDeserialize
public class FaceRecognizeResponse extends DeepStackResponse {

    Predictions[] predictions;

    @Getter
    @NoArgsConstructor
    @JsonDeserialize
    public static class Predictions {
        @JsonProperty("userid")
        String userId;
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
