package org.flaad.deepstack.client.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.flaad.deepstack.client.client.DeepStackClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeepStackConfiguration {

    @Configuration
    @EnableFeignClients(clients = DeepStackClient.class)
    public static class DeepStackFeignConfiguration {
    }
}
