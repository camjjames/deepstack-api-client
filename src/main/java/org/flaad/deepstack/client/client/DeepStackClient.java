package org.flaad.deepstack.client.client;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.flaad.deepstack.client.model.DeepStackFaceDetectionResponse;
import org.flaad.deepstack.client.model.DeepStackObjectDetectionResponse;
import org.flaad.deepstack.client.model.DeepStackSceneDetectionResponse;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@FeignClient(name = "${deepstack.api.name}",
        url = "${deepstack.api.url}",
        path = "${deepstack.api.path}",
        configuration = DeepStackClient.DeepStackClientConfig.class)
public interface DeepStackClient {

    @PostMapping(path = "/face", consumes = MULTIPART_FORM_DATA_VALUE)
    DeepStackFaceDetectionResponse faceDetection(@RequestPart("image") MultipartFile image);

    @PostMapping(path = "/detection", consumes = MULTIPART_FORM_DATA_VALUE)
    DeepStackObjectDetectionResponse objectDetection(@RequestPart("image") MultipartFile image);

    @PostMapping(path = "/scene", consumes = MULTIPART_FORM_DATA_VALUE)
    DeepStackSceneDetectionResponse sceneDetection(@RequestPart("image") MultipartFile image);


    /* Config for the feign client */
    class DeepStackClientConfig {

        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        public Decoder feignDecoder() {
            return new SpringDecoder(messageConverters);
        }

        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }

    }
}
