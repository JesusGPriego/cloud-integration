package com.suleware.microservices.loans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.suleware.microservices.loans.dto.LoansContactInfoDto;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableConfigurationProperties(value = { LoansContactInfoDto.class })
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(info = @Info(title = "Loans microservice REST API Documentation", description = "EazyBank Loans microservice REST API Documentation", version = "v1", contact = @Contact(name = "Jesús García", email = "notarealmail@mail.com", url = "https://localhost:8090"), license = @License(name = "Apache 2.0", url = "https://localhost:8090")), externalDocs = @ExternalDocumentation(description = "EazyBank Loans microservice REST API Documentation", url = "https://localhost:8090/swagger-ui.html"))
public class LoansApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoansApplication.class, args);
	}

}
