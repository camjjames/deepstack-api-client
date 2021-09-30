package org.flaad.deepstack.client.client;

import org.flaad.deepstack.client.model.FaceDetectionResponse;
import org.flaad.deepstack.client.model.ObjectDetectionResponse;
import org.flaad.deepstack.client.model.SceneDetectionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@FeignClient(name = "${deepstack.api.name}",url = "${deepstack.api.url}",path = "${deepstack.api.path}")
public interface DeepStackClient {

    @PostMapping(path = "/face/", consumes = MULTIPART_FORM_DATA_VALUE)
    FaceDetectionResponse faceDetection(@RequestPart("image") MultipartFile image);

    @PostMapping(path = "/detection", consumes = MULTIPART_FORM_DATA_VALUE)
    ObjectDetectionResponse objectDetection(@RequestPart("image") MultipartFile image);

    @PostMapping(path = "/scene", consumes = MULTIPART_FORM_DATA_VALUE)
    SceneDetectionResponse sceneDetection(@RequestPart("image") MultipartFile image);

}
