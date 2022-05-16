package com.dashboard.doctor_dashboard.exceptions;

import org.springframework.http.HttpStatus;


public class APIException extends RuntimeException {

    public final HttpStatus status;
    public final String message;

    public APIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
