package com.dashboard.doctor_dashboard.exception;

import org.springframework.http.HttpStatus;


public class APIException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public APIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
