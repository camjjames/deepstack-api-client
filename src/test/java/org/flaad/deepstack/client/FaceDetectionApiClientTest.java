package org.flaad.deepstack.client;

import org.flaad.deepstack.client.model.FaceDetectionResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.flaad.deepstack.client.TestUtilities.getImage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class FaceDetectionApiClientTest extends DeepStackApiClient {

    @Test
    void whenPostFaceDetection_thenFacesAreReturned() throws IOException, URISyntaxException {
        setupWireMock_FaceDetection("stubs/get-success-face-detection.json");
        FaceDetectionResponse response = dsClient.faceDetection(getImage("images/detect-face1.jpg"));
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getError(), is(nullValue()));
        assertThat(response.getPredictions(), is(notNullValue()));
        assertThat(response.getPredictions().size(), is(4));
        assertThat(response.getPredictions().get(0).getConfidence(), is(0.99990666));
        assertThat(response.getPredictions().get(0).getYMin(), is(145));
        assertThat(response.getPredictions().get(0).getXMin(), is(626));
        assertThat(response.getPredictions().get(0).getYMax(), is(261));
        assertThat(response.getPredictions().get(0).getXMax(), is(712));
    }

    @Test
    void whenPostFaceDetectionWithMinConfidence_thenFacesAreReturned() throws IOException, URISyntaxException {
        setupWireMock_FaceDetection("stubs/get-success-face-detection-min-confidence.json");
        FaceDetectionResponse response = dsClient.faceDetection(getImage("images/detect-face1.jpg"), 0.80);
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getError(), is(nullValue()));
        assertThat(response.getPredictions(), is(notNullValue()));
        assertThat(response.getPredictions().size(), is(2));
    }

    @Test
    void whenPostFaceDetection_thenFaceDetectionFails() throws IOException, URISyntaxException {
        setupWireMock_FaceDetection("stubs/get-failed-face-detection.json");
        FaceDetectionResponse response = dsClient.faceDetection(getImage("images/detect-face1.jpg"));
        assertThat(response.isSuccess(), is(false));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getPredictions(), is(nullValue()));
        assertThat(response.getError(), is(notNullValue()));
        assertThat(response.getError(), is("No valid image file found"));
    }

}
