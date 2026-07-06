package com.polytech.commandes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI commandesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestion de Commandes")
                        .description("API REST pour la gestion des produits, clients et commandes TP Spring Boot")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Equipe TP Spring Boot")));
    }
}