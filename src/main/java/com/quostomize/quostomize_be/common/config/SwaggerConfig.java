package com.quostomize.quostomize_be.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "커스텀 프로젝트 API 명세서",
                description = "커스텀 API 명세서",
                version = "v1")
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

//        String accessHeaderName = "access";
//        String refreshHeaderName = "refresh";
//
//        // Define the SecurityRequirement to be included in the request
//        SecurityRequirement securityRequirement = new SecurityRequirement()
//                .addList(accessHeaderName)
//                .addList(refreshHeaderName);
//
//        Components components = new Components()
//                .addSecuritySchemes(accessHeaderName, new SecurityScheme()
//                        .name(accessHeaderName)
//                        .type(SecurityScheme.Type.APIKEY)
//                        .in(SecurityScheme.In.HEADER)
//                        .name(accessHeaderName))
//                .addSecuritySchemes(refreshHeaderName, new SecurityScheme()
//                        .name(refreshHeaderName)
//                        .type(SecurityScheme.Type.APIKEY)
//                        .in(SecurityScheme.In.HEADER)
//                        .name(refreshHeaderName));

        return new OpenAPI()
                .addServersItem(new Server().url("https://quostomize.site").description("운영 서버"))
                .addServersItem(new Server().url("http://13.124.43.174:8080").description("AWS Server"))
                .addServersItem(new Server().url("http://localhost:8080").description("Local Server"))
//                .addSecurityItem(securityRequirement)
//                .components(components);
        ;
    }
}
