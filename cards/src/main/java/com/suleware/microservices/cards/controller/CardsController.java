package com.suleware.microservices.cards.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suleware.microservices.cards.constants.CardsConstants;
import com.suleware.microservices.cards.dto.CardsContactInfoDto;
import com.suleware.microservices.cards.dto.CardsDto;
import com.suleware.microservices.cards.dto.ErrorResponseDto;
import com.suleware.microservices.cards.dto.ResponseDto;
import com.suleware.microservices.cards.service.ICardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Eazy Bytes
 */

@Tag(name = "CRUD REST APIs for Cards in EazyBank", description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE card details")
@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE })
@AllArgsConstructor
@Validated
public class CardsController {

        private ICardService service;

        @Autowired
        private Environment env;

        @Autowired
        private CardsContactInfoDto cardsContactInfoDto;

        @Operation(summary = "Create Card REST API", description = "REST API to create new Card inside EazyBank")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        @PostMapping("/create")
        public ResponseEntity<ResponseDto> create(@RequestParam String mobileNumber) {

                service.createCard(mobileNumber);

                return ResponseEntity.status(HttpStatus.OK)
                                .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
        }

        @Operation(summary = "Fetch Card Details REST API", description = "REST API to fetch card details based on a mobile number")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        @GetMapping("/fetch")
        public ResponseEntity<CardsDto> fetch(@RequestParam String mobileNumber) {
                return ResponseEntity.status(HttpStatus.OK).body(service.fetchCard(mobileNumber));
        }

        @Operation(summary = "Update Card Details REST API", description = "REST API to update card details based on a card number")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                        @ApiResponse(responseCode = "417", description = "Expectation Failed"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        @PutMapping("/update")
        public ResponseEntity<ResponseDto> putMethodName(@Valid @RequestBody CardsDto cardsDto) {
                boolean isUpdated = service.updateCard(cardsDto);
                if (isUpdated) {
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
                }

                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                                .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));

        }

        @Operation(summary = "Delete Card Details REST API", description = "REST API to delete Card details based on a mobile number")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                        @ApiResponse(responseCode = "417", description = "Expectation Failed"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        @DeleteMapping("/delete")
        public ResponseEntity<ResponseDto> delete(@RequestParam String mobileNumber) {
                boolean isDeleted = service.deleteCard(mobileNumber);
                if (isDeleted) {
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
                }

                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                                .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));

        }

        @GetMapping("/java-version")
        public ResponseEntity<String> getContactInfo() {

                return ResponseEntity.ok().body(env.getProperty("JAVA_HOME"));
        }

        @Operation(summary = "Get Contact Info", description = "Get Contact Info")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")

        @GetMapping("/contact-info")
        public ResponseEntity<CardsContactInfoDto> envVersion() {

                return ResponseEntity.ok().body(cardsContactInfoDto);
        }

}
