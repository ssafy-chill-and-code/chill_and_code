package com.ssafy.chillandcode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI chillAndCodeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Chill & Code API Documentation")
                        .description("워크케이션 추천 · 일정 관리 · 커뮤니티 기능을 제공하는 Chill & Code REST API 명세서")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Chill & Code Project")
                                .url("https://github.com/ssafy-chill-and-code/chill_and_code")
                        )
                );
    }
}
