package com.devops.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;

@Configuration
public class BeanConfig {
    @Bean
    public R2dbcMappingContext r2dbcMappingContext() {
        return new R2dbcMappingContext();
    }
}
