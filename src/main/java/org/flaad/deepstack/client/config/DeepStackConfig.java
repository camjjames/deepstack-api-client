package org.flaad.deepstack.client.config;

import org.flaad.deepstack.client.client.DeepStackClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableFeignClients(clients = DeepStackClient.class)
public class DeepStackConfig {
}
