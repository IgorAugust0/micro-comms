package com.igor.microcomms.products_api.config.exception;

// import lombok.AllArgsConstructor;
import lombok.Data;

@Data
// @AllArgsConstructor // uncomment this if using the commented return in GlobalExceptionHandler methods
public class ExceptionDetails {

    private int status;
    private String message;
}
