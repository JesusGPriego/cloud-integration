package com.suleware.microservices.loans.controller;

import org.springframework.web.bind.annotation.RestController;

import com.suleware.microservices.loans.constants.LoansConstants;
import com.suleware.microservices.loans.dto.ErrorResponseDto;
import com.suleware.microservices.loans.dto.LoansContactInfoDto;
import com.suleware.microservices.loans.dto.LoansDto;
import com.suleware.microservices.loans.dto.ResponseDto;
import com.suleware.microservices.loans.service.ILoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "CRUD REST APIs for Loans", description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE loan details")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE })
public class LoansController {

        private ILoanService service;

        @Autowired
        private Environment env;

        @Autowired
        private LoansContactInfoDto loansContactInfoDto;

        @GetMapping("/list")
        public ResponseEntity<List<LoansDto>> getMethodName() {

                return ResponseEntity.status(HttpStatus.OK).body(service.findAll());

        }

        @Operation(summary = "Create Loan REST API", description = "REST API to create new loan inside EazyBank")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        @PostMapping("/create")
        public ResponseEntity<ResponseDto> createLoaEntity(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{9})", message = "Mobile number must be 9 digits") String mobileNumber) {
                service.createLoan(mobileNumber);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
        }

        @Operation(summary = "Fetch Loan Details REST API", description = "REST API to fetch loan details based on a mobile number")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        @GetMapping("/fetch")
        public ResponseEntity<LoansDto> fetchLoans(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{9})", message = "Mobile number must be 9 digits") String mobileNumber) {

                return ResponseEntity.status(HttpStatus.OK).body(service.fetchLoan(mobileNumber));
        }

        @Operation(summary = "Update Loan Details REST API", description = "REST API to update loan details based on a loan number")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                        @ApiResponse(responseCode = "417", description = "Expectation Failed"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        @PutMapping("/update")
        public ResponseEntity<ResponseDto> putMethodName(@Valid @RequestBody LoansDto loansDto) {
                boolean isUpdated = service.updateLoan(loansDto);
                if (isUpdated) {
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
                }
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                                .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE));
        }

        @Operation(summary = "Delete Loan Details REST API", description = "REST API to delete Loan details based on a mobile number")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                        @ApiResponse(responseCode = "417", description = "Expectation Failed"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        @DeleteMapping("/delete")
        public ResponseEntity<ResponseDto> delete(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{9})", message = "Mobile number must be 9 digits") String mobileNumber) {
                boolean isDeleted = service.deleteLoan(mobileNumber);
                if (isDeleted) {
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
                }
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                                .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));

        }

        @GetMapping("/java-version")
        public ResponseEntity<String> getContactInfo() {

                return ResponseEntity.ok().body(env.getProperty("JAVA_HOME"));
        }

        @Operation(summary = "Get Contact Info", description = "Get Contact Info")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")

        @GetMapping("/contact-info")
        public ResponseEntity<LoansContactInfoDto> envVersion() {

                return ResponseEntity.ok().body(loansContactInfoDto);
        }

}
