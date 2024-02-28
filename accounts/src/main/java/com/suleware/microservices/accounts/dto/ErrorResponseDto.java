package com.suleware.microservices.accounts.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(name = "ErrorResponse", description = "Schema to hold error response information")
@Data
@AllArgsConstructor
public class ErrorResponseDto {

    @Schema(description = "Api path invoked by client", example = "/api/create")
    private String apiPath;

    @Schema(description = "Error code representing the error happened", example = "200")
    private HttpStatus errorcode;

    @Schema(description = "Error message explaining the error happened", example = "Delete operation failed. Please try again or contact Dev team")
    private String errorMessage;

    @Schema(description = "Error time representing the time when error happened")
    private LocalDateTime errorTime;

}
