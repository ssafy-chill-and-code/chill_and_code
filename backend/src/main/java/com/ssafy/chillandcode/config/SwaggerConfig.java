package com.ssafy.chillandcode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI chillAndCodeOpenAPI() {

        // 🔐 JWT 보안 스키마 정의
        SecurityScheme jwtScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // 🔐 전역 보안 요구사항
        SecurityRequirement securityRequirement =
                new SecurityRequirement().addList("JWT");

        return new OpenAPI()
                .info(new Info()
                        .title("Chill & Code API Documentation")
                        .description("워크케이션 추천 · 일정 관리 · 커뮤니티 기능을 제공하는 Chill & Code REST API 명세서")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Chill & Code Project")
                                .url("https://github.com/ssafy-chill-and-code/chill_and_code")
                        )
                )
                // 🔐 Swagger에 JWT 등록
                .schemaRequirement("JWT", jwtScheme)
                .addSecurityItem(securityRequirement);
    }
}
