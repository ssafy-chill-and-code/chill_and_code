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
                        .description("?크케?션 추천 · ?정 관?· 커?티 기능???공?는 Chill & Code REST API 명세?")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Chill & Code Project")
                                .url("https://github.com/ssafy-chill-and-code/chill_and_code")
                        )
                );
    }
}
