package org.flaad.deepstack.client;

import org.apache.commons.io.IOUtils;
import org.flaad.deepstack.client.client.DeepStackClient;
import org.flaad.deepstack.client.config.DeepStackConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.aMultipart;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@ActiveProfiles("test")
@EnableAutoConfiguration
@AutoConfigureWireMock(port = DeepStackApiClient.port)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { DeepStackConfiguration.class })
class DeepStackApiClient {

    public static final int port = 9561;

    @Autowired
    DeepStackClient dsClient;


    /* Set up WireMock for face detection */
    void setupWireMock_FaceDetection(String location) throws IOException {
        stubFor(post(urlEqualTo("/v1/vision/face/"))
                .withHeader("Content-Type", containing("multipart/form-data; charset=UTF-8;"))
                .withMultipartRequestBody(aMultipart())
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(read(location))));
    }

    /* Set up WireMock for face registration */
    void setupWireMock_FaceRegister(String location) throws IOException {
        stubFor(post(urlEqualTo("/v1/vision/face/register"))
                .withHeader("Content-Type", containing("multipart/form-data; charset=UTF-8;"))
                .withMultipartRequestBody(aMultipart())
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(read(location))));
    }

    /* Set up WireMock for face recognition */
    void setupWireMock_FaceRecognize(String location) throws IOException {
        stubFor(post(urlEqualTo("/v1/vision/face/recognize"))
                .withHeader("Content-Type", containing("multipart/form-data; charset=UTF-8;"))
                .withMultipartRequestBody(aMultipart())
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(read(location))));
    }


    /* Set up WireMock for object detection */
    void setupWireMock_ObjectDetection(String location) throws IOException {
        stubFor(post(urlEqualTo("/v1/vision/detection"))
                .withHeader("Content-Type", containing("multipart/form-data; charset=UTF-8;"))
                .withMultipartRequestBody(aMultipart())
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(read(location))));
    }

    /* Set up WireMock for object recognition */
    void setupWireMock_SceneRecognition(String location) throws IOException {
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
