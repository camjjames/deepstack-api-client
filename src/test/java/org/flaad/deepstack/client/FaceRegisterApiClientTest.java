package org.flaad.deepstack.client;

import org.flaad.deepstack.client.model.FaceRegisterResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.flaad.deepstack.client.TestUtilities.getImage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class FaceRegisterApiClientTest extends DeepStackApiClient {

    @Test
    void whenPostFaceRegister_thenSuccessIsReturned() throws IOException, URISyntaxException {
        setupWireMock_FaceRegister("stubs/get-success-face-register.json");
        FaceRegisterResponse response = dsClient.faceRegister(getImage("images/register-face1.jpg"), "Daniel Andrews");
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getMessage(), is("face added"));
        assertThat(response.getError(), is(nullValue()));
    }

    @Test
    void whenPostFaceRegister_thenUserIdNotFoundReturned() throws IOException, URISyntaxException {
        setupWireMock_FaceRegister("stubs/get-failed-face-detection-userid-not-found.json");
        FaceRegisterResponse response = dsClient.faceRegister(getImage("images/register-face1.jpg"), "Billy Bob");
        assertThat(response.isSuccess(), is(false));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getMessage(), is(nullValue()));
        assertThat(response.getError(), is(notNullValue()));
        assertThat(response.getError(), is("userid not specified"));
    }

    @Test
    void whenPostFaceRegister_thenImageNotFoundFails() throws IOException, URISyntaxException {
        setupWireMock_FaceRegister("stubs/get-failed-face-detection-no-image-found.json");
        FaceRegisterResponse response = dsClient.faceRegister(getImage("images/register-face1.jpg"), "Luck Skywalker");
        assertThat(response.isSuccess(), is(false));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getMessage(), is(nullValue()));
        assertThat(response.getError(), is(notNullValue()));
        assertThat(response.getError(), is("No valid image file found"));
    }

}
