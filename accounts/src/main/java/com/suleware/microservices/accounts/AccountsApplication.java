package com.suleware.microservices.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.suleware.microservices.accounts.dto.AccountsContactInfoDto;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = { AccountsContactInfoDto.class })
@OpenAPIDefinition(info = @Info(title = "Accounts microservice REST API Documentation", description = "Accounts Microservice REST API Documentation", version = "v1", contact = @Contact(name = "Jesús García", email = "notMyRealMail@notrealmail.notemail", url = "suleware.com"), license = @License(name = "Apache 2.0")), externalDocs = @ExternalDocumentation(description = "Accounts Microservice REST API Doc", url = "suleware.com"))
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
