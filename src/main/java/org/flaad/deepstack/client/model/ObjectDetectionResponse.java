package org.flaad.deepstack.client.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@JsonDeserialize
public class ObjectDetectionResponse extends DeepStackResponse {

    List<Prediction> predictions;

}
