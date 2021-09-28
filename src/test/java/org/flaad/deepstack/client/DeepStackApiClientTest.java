package org.flaad.deepstack.client;

import org.apache.commons.io.IOUtils;
import org.flaad.deepstack.client.client.DeepStackClient;
import org.flaad.deepstack.client.config.DeepStackConfig;
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
    void whenGetObjDetection_thenDetectedObjectIsReturned() throws IOException, URISyntaxException {
        setupWireMock_ObjectDetection("stubs/get-success-object-detection.json");

        DeepStackObjectDetectionResponse response = dsClient.objectDetection(getImage("images/test-image1.jpg"));

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
    void whenGetObjDetection_thenDetectedObjectFails() throws IOException, URISyntaxException {
        setupWireMock_ObjectDetection("stubs/get-failed-object-detection.json");

        DeepStackObjectDetectionResponse response = dsClient.objectDetection(getImage("images/test-image1.jpg"));

        assertThat(response.isSuccess(), is(false));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getPredictions(), is(nullValue()));
        assertThat(response.getError(), is(notNullValue()));
        assertThat(response.getError(), is("No valid image file found"));
    }


    @Test
    void whenGetSceneDetection_thenOfficeSceneIsReturned() throws IOException, URISyntaxException {
        setupWireMock_SceneDetection("stubs/get-success-office-scene-detection.json");

        DeepStackSceneDetectionResponse response = dsClient.sceneDetection(getImage("images/test-image2.jpg"));

        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getConfidence(), is(0.4986186));
        assertThat(response.getLabel(), is("office_cubicles"));
    }

    @Test
    void whenGetSceneDetection_thenBuildingSceneIsReturned() throws IOException, URISyntaxException {
        setupWireMock_SceneDetection("stubs/get-success-building-scene-detection.json");

        DeepStackSceneDetectionResponse response = dsClient.sceneDetection(getImage("images/test-image3.jpg"));

        assertThat(response.isSuccess(), is(true));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getConfidence(), is(0.4986186));
        assertThat(response.getLabel(), is("apartment_building/outdoor"));
    }

    @Test
    void whenGetSceneDetection_thenDetectedSceneFails() throws IOException, URISyntaxException {
        setupWireMock_SceneDetection("stubs/get-failed-scene-detection.json");

        DeepStackSceneDetectionResponse response = dsClient.sceneDetection(getImage("images/test-image1.jpg"));

        assertThat(response.isSuccess(), is(false));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getError(), is(notNullValue()));
        assertThat(response.getError(), is("No valid image file found"));
    }


    /* Setup the WireMock used for this Test */
    private void setupWireMock_ObjectDetection(String location) throws IOException {
        stubFor(post(urlEqualTo("/v1/vision/detection"))
                .withHeader("Content-Type", containing("multipart/form-data; charset=UTF-8;"))
                .withMultipartRequestBody(aMultipart())
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(read(location))));
    }

    /* Setup the WireMock used for this Test */
    private void setupWireMock_SceneDetection(String location) throws IOException {
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
