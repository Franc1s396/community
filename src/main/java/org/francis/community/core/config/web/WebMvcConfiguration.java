package org.francis.community.core.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Franc1s
 * @date 2022/11/14
 * @apiNote
 */
@Configuration
public class WebMvcConfiguration {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
