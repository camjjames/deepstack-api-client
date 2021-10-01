package org.flaad.deepstack.client.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonDeserialize
public class DeepStackResponse {

    boolean success;
    int duration;

    // Error is optional, only returned when an error occurs
    String error;
}
