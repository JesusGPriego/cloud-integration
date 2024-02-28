package com.suleware.microservices.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(name = "Response", description = "Schema to hold succesful response information")
@Data
@AllArgsConstructor
public class ResponseDto {
    @Schema(description = "Response code status", example = "200")
    private String statusCode;

    @Schema(description = "Schema to hold succesful response information", example = "Request processed successfully")
    private String statusMsg;

}
