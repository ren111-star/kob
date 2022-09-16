package com.kob.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean  // 如果要加载到service，就要用bean
    public RestTemplate getRestTemplate () {
        return new RestTemplate();
    }
}
