package org.flaad.deepstack.client.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonDeserialize
public class FaceRegisterResponse extends DeepStackResponse {

    String message;

}
