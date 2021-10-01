package org.flaad.deepstack.client;

import org.flaad.deepstack.client.model.ObjectDetectionResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.flaad.deepstack.client.TestUtilities.getImage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class ObjectDetectionApiClientTest extends DeepStackApiClient {

    @Test
    void whenPostObjDetection_thenDetectedObjectIsReturned() throws IOException, URISyntaxException {
        setupWireMock_ObjectDetection("stubs/get-success-object-detection.json");
        ObjectDetectionResponse response = dsClient.objectDetection(getImage("images/detect-object1.jpg"));
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
        ObjectDetectionResponse response = dsClient.objectDetection(getImage("images/detect-object1.jpg"));
        assertThat(response.isSuccess(), is(false));
        assertThat(response.getDuration(), is(0));
        assertThat(response.getPredictions(), is(nullValue()));
        assertThat(response.getError(), is(notNullValue()));
        assertThat(response.getError(), is("No valid image file found"));
    }

}
