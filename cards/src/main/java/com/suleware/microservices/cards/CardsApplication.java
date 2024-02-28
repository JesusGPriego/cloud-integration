package com.suleware.microservices.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.suleware.microservices.cards.dto.CardsContactInfoDto;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = { CardsContactInfoDto.class })
@OpenAPIDefinition(info = @Info(title = "Cards microservice REST API Documentation", description = "Cards microservice REST API Documentation", version = "v1", contact = @Contact(name = "Jesús García Priego", email = "notrealmail@mail.com", url = "https://www.eazybytes.com"), license = @License(name = "Apache 2.0", url = "localhost:9000")), externalDocs = @ExternalDocumentation(description = "EazyBank Cards microservice REST API Documentation", url = "https://localhost:9000/swagger-ui.html"))

public class CardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardsApplication.class, args);
	}

}
