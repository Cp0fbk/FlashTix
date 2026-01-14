package com.flashtix.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

        @Value("${server.port:8080}")
        private String serverPort;

        @Bean
        public OpenAPI customOpenAPI() {
                // Define the JWT security scheme name
                String securitySchemeName = "Bearer-JWT";

                return new OpenAPI()
                                .info(new Info()
                                                .title("FlashTix API")
                                                .version("1.0.0")
                                                .description("FlashTix - Ticket Sales Website API Documentation\n\n" +
                                                                "**Authentication**: Login via `/api/auth/login` to obtain a JWT token. "
                                                                +
                                                                "Use the 'Authorize' button to add the token for protected endpoints.")
                                                .contact(new Contact()
                                                                .name("FlashTix Team")
                                                                .email("support@flashtix.com"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                                .servers(List.of(
                                                new Server()
                                                                .url("http://localhost:" + serverPort)
                                                                .description("Local Development Server")))
                                .components(new Components()
                                                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")
                                                                .description("Enter JWT token obtained from /api/auth/login endpoint")))
                                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
        }
}
