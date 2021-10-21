package org.flaad.deepstack.client;

import org.flaad.deepstack.client.model.FaceRecognizeResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.flaad.deepstack.client.TestUtilities.getImage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class FaceRecognizeApiClientTest extends DeepStackApiClient {

    @Test
    void whenPostFaceRecognize_thenSuccessNoPredictionsAreReturned() throws IOException, URISyntaxException {
        setupWireMock_FaceRecognize("stubs/get-success-face-recognition.json");
        FaceRecognizeResponse response = dsClient.faceRecognize(getImage("images/register-face1.jpg"));
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getError(), is(nullValue()));
        assertThat(response.getPredictions(), is(notNullValue()));
        assertThat(response.getPredictions().size(), is(1));
        assertThat(response.getPredictions().get(0).getConfidence(), is(1.0));
        assertThat(response.getPredictions().get(0).getUserId(), is("Daniel Andrews"));
        assertThat(response.getPredictions().get(0).getYMin(), is(24));
        assertThat(response.getPredictions().get(0).getXMin(), is(104));
        assertThat(response.getPredictions().get(0).getYMax(), is(461));
        assertThat(response.getPredictions().get(0).getXMax(), is(451));
    }

    @Test
    void whenPostFaceRecognize_thenUserIdNotFoundReturned() throws IOException, URISyntaxException {
        setupWireMock_FaceRecognize("stubs/get-success-face-recognition-no-predictions.json");
        FaceRecognizeResponse response = dsClient.faceRecognize(getImage("images/register-face1.jpg"));
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getError(), is(nullValue()));
        assertThat(response.getPredictions(), is(notNullValue()));
        assertThat(response.getPredictions().size(), is(0));
    }

    @Test
    void whenPostFaceRegister_thenImageNotFoundFails() throws IOException, URISyntaxException {
        setupWireMock_FaceRecognize("stubs/get-failed-face-recognition.json");
        FaceRecognizeResponse response = dsClient.faceRecognize(getImage("images/register-face1.jpg"));
        assertThat(response.isSuccess(), is(false));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getError(), is(notNullValue()));
        assertThat(response.getError(), is("No valid image file found"));
        assertThat(response.getPredictions(), is(nullValue()));
    }

}
