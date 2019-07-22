package com.abril.tvapi.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class GlobalConfig {

    @Getter
    @Value("${redis.host}")
    private String redisHost;

    @Getter
    @Value("${redis.port}")
    private Integer redisPort;

    @Getter
    @Value("${jwt-seceret}")
    private String jwtSecret;
}
