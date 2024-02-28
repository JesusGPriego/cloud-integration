package com.suleware.microservices.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Customer", description = "Schema to hold Customer and Account info")
public class CustomerDto {

    @Schema(description = "This is the customer name", example = "Peter")
    @NotEmpty
    @Size(min = 5, max = 25, message = "Name length should be between 5 and 25")
    private String name;

    @Schema(description = "This is the customer email", example = "example@mail.com")
    @NotEmpty
    @Email
    private String email;

    @Schema(description = "This is the customer mobile number. It has to be 9 digits", example = "123456789")
    @NotEmpty
    @Pattern(regexp = "$|[0-9]{9}", message = "Mobile number can only have digits, and its length should be 9")
    private String mobileNumber;

    @Schema(description = "Account details of the customer")
    private AccountsDto accountsDto;

}
