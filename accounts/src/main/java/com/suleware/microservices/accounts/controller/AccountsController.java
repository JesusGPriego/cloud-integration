package com.suleware.microservices.accounts.controller;

import org.springframework.web.bind.annotation.RestController;

import com.suleware.microservices.accounts.constants.AccountsConstants;
import com.suleware.microservices.accounts.dto.AccountsContactInfoDto;
import com.suleware.microservices.accounts.dto.CustomerDto;
import com.suleware.microservices.accounts.dto.ErrorResponseDto;
import com.suleware.microservices.accounts.dto.ResponseDto;
import com.suleware.microservices.accounts.service.IAccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "CRUD RESTA APIs", description = "CRUD REST APIs for a regular bank CRUD")
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AccountsController {

        @Value("${build.version}")
        private String buildVersion;

        @Autowired
        private AccountsContactInfoDto accountsContactInfoDto;

        private Environment env;

        private IAccountService accountService;

        public AccountsController(IAccountService accountService, Environment env) {
                this.accountService = accountService;
                this.env = env;
        }

        @Operation(summary = "Lists all accounts and customers REST API", description = "Lists all db content Customer Account")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")
        @GetMapping("/list")
        public ResponseEntity<List<CustomerDto>> list() {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(accountService.findAll());

        }

        @Operation(summary = "Create Account REST API", description = "Create new Customer Account")
        @ApiResponse(responseCode = "201", description = "HTTP Status CREATED")
        @PostMapping("/create")
        public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {

                accountService.createAccount(customerDto);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));

        }

        @Operation(summary = "Update Account REST API", description = "Update new Customer Account")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")
        @GetMapping("/fetch")
        public ResponseEntity<CustomerDto> fetchAccountDetail(
                        @RequestParam @Pattern(regexp = "$|[0-9]{9}", message = "Mobile number can only have digits, and its length should be 9") String mobileNumber) {
                CustomerDto customerDto = accountService.fetchAccount(mobileNumber);

                return ResponseEntity.status(HttpStatus.OK)
                                .body(customerDto);
        }

        @Operation(summary = "Update Account REST API", description = "Update existing Customer Account")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))

        })
        @PutMapping("/update")
        public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
                boolean isUpdated = accountService.updateAccount(customerDto);

                if (isUpdated) {
                        return ResponseEntity
                                        .status(HttpStatus.OK)
                                        .body(new ResponseDto(AccountsConstants.STATUS_200,
                                                        AccountsConstants.MESSAGE_200));
                } else {
                        return ResponseEntity
                                        .status(HttpStatus.EXPECTATION_FAILED)
                                        .body(new ResponseDto(AccountsConstants.STATUS_417,
                                                        AccountsConstants.MESSAGE_417_UPDATE));

                }
        }

        @Operation(summary = "Deletes Account REST API", description = "Deletes existing Customer Account")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error")

        })
        @DeleteMapping("/delete")
        public ResponseEntity<ResponseDto> deleteAccountDetails(
                        @RequestParam @Pattern(regexp = "$|[0-9]{9}", message = "Mobile number can only have digits, and its length should be 9") String mobileNumber) {

                boolean isDeleted = accountService.deleteAccount(mobileNumber);

                if (isDeleted) {
                        return ResponseEntity
                                        .status(HttpStatus.OK)
                                        .body(new ResponseDto(AccountsConstants.STATUS_200,
                                                        AccountsConstants.MESSAGE_200));
                }
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.MESSAGE_500));

        }

        @Operation(summary = "Shows Java version", description = "Shows Java version")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")
        @GetMapping("/build-info")
        public ResponseEntity<String> buildVersion() {
                return ResponseEntity.ok().body(buildVersion);
        }

        @Operation(summary = "Shows API version", description = "Shows API version")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")

        @GetMapping("/java-version")
        public ResponseEntity<String> getContactInfo() {

                return ResponseEntity.ok().body(env.getProperty("JAVA_HOME"));
        }

        @Operation(summary = "Get Contact Info", description = "Get Contact Info")
        @ApiResponse(responseCode = "200", description = "HTTP Status OK")

        @GetMapping("/contact-info")
        public ResponseEntity<AccountsContactInfoDto> envVersion() {

                return ResponseEntity.ok().body(accountsContactInfoDto);
        }

}
