package org.flaad.deepstack.client;

import org.flaad.deepstack.client.model.SceneDetectionResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.flaad.deepstack.client.TestUtilities.getImage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class SceneDetectionApiClientTest extends DeepStackApiClient {

    @Test
    void whenPostSceneDetection_thenOfficeSceneIsReturned() throws IOException, URISyntaxException {
        setupWireMock_SceneRecognition("stubs/get-success-office-scene-detection.json");
        SceneDetectionResponse response = dsClient.sceneDetection(getImage("images/detect-scene1.jpg"));
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getConfidence(), is(0.6666666));
        assertThat(response.getLabel(), is("office_cubicles"));
    }

    @Test
    void whenPostSceneDetection_thenBuildingSceneIsReturned() throws IOException, URISyntaxException {
        setupWireMock_SceneRecognition("stubs/get-success-building-scene-detection.json");
        SceneDetectionResponse response = dsClient.sceneDetection(getImage("images/detect-scene2.jpg"));
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getConfidence(), is(0.4986186));
        assertThat(response.getLabel(), is("apartment_building/outdoor"));
    }

    @Test
    void whenPostSceneDetection_thenDetectedSceneFails() throws IOException, URISyntaxException {
        setupWireMock_SceneRecognition("stubs/get-failed-scene-detection.json");
        SceneDetectionResponse response = dsClient.sceneDetection(getImage("images/detect-scene1.jpg"));
        assertThat(response.isSuccess(), is(false));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getError(), is(notNullValue()));
        assertThat(response.getError(), is("No valid image file found"));
    }

}
