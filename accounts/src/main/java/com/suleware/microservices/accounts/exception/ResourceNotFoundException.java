package com.suleware.microservices.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourcename, String fieldName, String fieldValue) {
        super(String.format("%s not found with the given input data %s: %s", resourcename, fieldName, fieldValue));
    }

}
