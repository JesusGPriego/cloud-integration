package com.suleware.microservices.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(name = "Accounts", description = "Schema to hold Customer's account details")
@Data
public class AccountsDto {

    @Schema(description = "Customer's account number", example = "123456789")
    @NotEmpty
    @Pattern(regexp = "$|[0-9]{9}", message = "Account number can only have digits, and its length should be 9")
    private Long accountNumber;

    @Schema(description = "Schema to hold Customer's account type", example = "Savings")
    @NotEmpty(message = "AccountType cannot be null or empty")
    private String accountType;

    @Schema(description = "Schema to hold Customer's account branch address", example = "123 Main Street, New York")
    @NotEmpty(message = "branchAddress cannot be null or empty")
    private String branchAddress;

}
