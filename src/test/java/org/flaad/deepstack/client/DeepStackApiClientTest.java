package org.flaad.deepstack.client;

import org.apache.commons.io.IOUtils;
import org.flaad.deepstack.client.client.DeepStackClient;
import org.flaad.deepstack.client.config.DeepStackConfig;
import org.flaad.deepstack.client.model.DeepStackFaceDetectionResponse;
import org.flaad.deepstack.client.model.DeepStackObjectDetectionResponse;
import org.flaad.deepstack.client.model.DeepStackSceneDetectionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.aMultipart;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.flaad.deepstack.client.TestUtilities.getImage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@ActiveProfiles("test")
@AutoConfigureWireMock(port = DeepStackApiClientTest.port)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { DeepStackConfig.class })
class DeepStackApiClientTest {

    public static final int port = 9561;

    @Autowired
    private DeepStackClient dsClient;

    @Test
    void whenPostObjDetection_thenDetectedObjectIsReturned() throws IOException, URISyntaxException {
        setupWireMock_ObjectDetection("stubs/get-success-object-detection.json");
        DeepStackObjectDetectionResponse response = dsClient.objectDetection(getImage("images/test-object1.jpg"));
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getError(), is(nullValue()));
        assertThat(response.getPredictions(), is(notNullValue()));
        assertThat(response.getPredictions().length, is(2));
        assertThat(response.getPredictions()[0].getLabel(), is("person"));
        assertThat(response.getPredictions()[0].getConfidence(), is(0.8618164));
        assertThat(response.getPredictions()[0].getYMin(), is(62));
        assertThat(response.getPredictions()[0].getXMin(), is(197));
        assertThat(response.getPredictions()[0].getYMax(), is(607));
        assertThat(response.getPredictions()[0].getXMax(), is(386));
    }

    @Test
    void whenPostObjDetection_thenDetectedObjectFails() throws IOException, URISyntaxException {
        setupWireMock_ObjectDetection("stubs/get-failed-object-detection.json");
        DeepStackObjectDetectionResponse response = dsClient.objectDetection(getImage("images/test-object1.jpg"));
        assertThat(response.isSuccess(), is(false));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getPredictions(), is(nullValue()));
        assertThat(response.getError(), is(notNullValue()));
        assertThat(response.getError(), is("No valid image file found"));
    }


    @Test
    void whenPostSceneDetection_thenOfficeSceneIsReturned() throws IOException, URISyntaxException {
        setupWireMock_SceneRecognition("stubs/get-success-office-scene-detection.json");
        DeepStackSceneDetectionResponse response = dsClient.sceneDetection(getImage("images/test-scene1.jpg"));
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getConfidence(), is(0.6666666));
        assertThat(response.getLabel(), is("office_cubicles"));
    }

    @Test
    void whenPostSceneDetection_thenBuildingSceneIsReturned() throws IOException, URISyntaxException {
        setupWireMock_SceneRecognition("stubs/get-success-building-scene-detection.json");
        DeepStackSceneDetectionResponse response = dsClient.sceneDetection(getImage("images/test-scene2.jpg"));
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getConfidence(), is(0.4986186));
        assertThat(response.getLabel(), is("apartment_building/outdoor"));
    }

    @Test
    void whenPostSceneDetection_thenDetectedSceneFails() throws IOException, URISyntaxException {
        setupWireMock_SceneRecognition("stubs/get-failed-scene-detection.json");
        DeepStackSceneDetectionResponse response = dsClient.sceneDetection(getImage("images/test-scene1.jpg"));
        assertThat(response.isSuccess(), is(false));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getError(), is(notNullValue()));
        assertThat(response.getError(), is("No valid image file found"));
    }


    @Test
    void whenPostFaceDetection_thenFacesAreReturned() throws IOException, URISyntaxException {
        setupWireMock_FaceDetection("stubs/get-success-face-detection.json");
        DeepStackFaceDetectionResponse response = dsClient.faceDetection(getImage("images/test-face1.jpg"));
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getError(), is(nullValue()));
        assertThat(response.getPredictions(), is(notNullValue()));
        assertThat(response.getPredictions().length, is(4));
        assertThat(response.getPredictions()[0].getConfidence(), is(0.99990666));
        assertThat(response.getPredictions()[0].getYMin(), is(145));
        assertThat(response.getPredictions()[0].getXMin(), is(626));
        assertThat(response.getPredictions()[0].getYMax(), is(261));
        assertThat(response.getPredictions()[0].getXMax(), is(712));
    }

    @Test
    void whenPostFaceDetection_thenFaceDetectionFails() throws IOException, URISyntaxException {
        setupWireMock_FaceDetection("stubs/get-failed-face-detection.json");
        DeepStackFaceDetectionResponse response = dsClient.faceDetection(getImage("images/test-face1.jpg"));
        assertThat(response.isSuccess(), is(false));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getPredictions(), is(nullValue()));
        assertThat(response.getError(), is(notNullValue()));
        assertThat(response.getError(), is("No valid image file found"));
    }



    /* set up the WireMock for face detection */
    private void setupWireMock_FaceDetection(String location) throws IOException {
        stubFor(post(urlEqualTo("/v1/vision/face"))
                .withHeader("Content-Type", containing("multipart/form-data; charset=UTF-8;"))
                .withMultipartRequestBody(aMultipart())
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(read(location))));
    }

    /* set up the WireMock for object detection */
    private void setupWireMock_ObjectDetection(String location) throws IOException {
        stubFor(post(urlEqualTo("/v1/vision/detection"))
                .withHeader("Content-Type", containing("multipart/form-data; charset=UTF-8;"))
                .withMultipartRequestBody(aMultipart())
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(read(location))));
    }

    /* set up the WireMock for object recognition */
    private void setupWireMock_SceneRecognition(String location) throws IOException {
        stubFor(post(urlEqualTo("/v1/vision/scene"))
                .withHeader("Content-Type", containing("multipart/form-data; charset=UTF-8;"))
                .withMultipartRequestBody(aMultipart())
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(read(location))));
    }


    /* Read a file from the filesystem and return its contents as a String */
    private String read(String location) throws IOException {
        return IOUtils.toString(new ClassPathResource(location).getInputStream(), StandardCharsets.UTF_8);
    }
}
