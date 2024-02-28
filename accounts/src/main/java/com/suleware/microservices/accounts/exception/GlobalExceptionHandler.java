package com.suleware.microservices.accounts.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suleware.microservices.accounts.dto.ErrorResponseDto;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

        @Autowired
        ObjectMapper objectMapper;

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                        HttpHeaders headers, HttpStatusCode status, WebRequest request) {
                String endpoint = request.getDescription(false);
                Map<String, String> validationErrors = new HashMap<>();
                List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

                validationErrorList.forEach(error -> {
                        String fieldName = ((FieldError) error).getField();
                        String validationMsg = error.getDefaultMessage();
                        validationErrors.put(fieldName, validationMsg);
                });

                try {
                        ErrorResponseDto errorDto = new ErrorResponseDto(
                                        endpoint,
                                        HttpStatus.BAD_REQUEST, objectMapper.writeValueAsString(validationErrors),
                                        LocalDateTime.now());
                        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
                } catch (JsonProcessingException e) {
                        e.printStackTrace();
                }

                return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);

        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
                        WebRequest request) {
                String endpoint = request.getDescription(false);
                Map<String, String> errors = new HashMap<>();
                ex.getConstraintViolations().forEach(constraintViolation -> {
                        String propertyPath = constraintViolation.getPropertyPath().toString();
                        errors.put(propertyPath.substring(propertyPath.indexOf(".") + 1),
                                        constraintViolation.getMessage());
                });
                try {
                        ErrorResponseDto errorDto = new ErrorResponseDto(endpoint,
                                        HttpStatus.BAD_REQUEST, objectMapper.writeValueAsString(
                                                        errors),
                                        LocalDateTime.now());
                        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
                } catch (JsonProcessingException e) {
                        e.printStackTrace();
                }
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponseDto> handleGlobalException(
                        Exception exception,
                        WebRequest webRequest) {
                ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                                webRequest.getDescription(false),
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                exception.getMessage(),
                                LocalDateTime.now());
                return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(
                        ResourceNotFoundException exception,
                        WebRequest webRequest) {
                ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                                webRequest.getDescription(false),
                                HttpStatus.NOT_FOUND,
                                exception.getMessage(),
                                LocalDateTime.now());
                return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(CustomerAlreadyExistsException.class)
        public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(
                        CustomerAlreadyExistsException exception,
                        WebRequest webRequest) {
                ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                                webRequest.getDescription(false),
                                HttpStatus.BAD_REQUEST,
                                exception.getMessage(),
                                LocalDateTime.now());
                return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
        }
}
