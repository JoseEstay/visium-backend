package com.visium.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(info())
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
				.components(
						new Components()
								.addSecuritySchemes("bearerAuth", bearerAuth()));
	}

	private Info info() {
		return new Info()
				.title("VISIUM Backend API")
				.version("1.0.0")
				.description("API REST para la plataforma VISIUM de gestión de ópticas")
				.contact(
						new Contact()
								.name("VISIUM")
								.email("visium@visium.dev"));
	}

	private SecurityScheme bearerAuth() {
		return new SecurityScheme()
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT")
				.in(SecurityScheme.In.HEADER)
				.name("Authorization");
	}
}