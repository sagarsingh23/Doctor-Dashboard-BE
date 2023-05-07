package com.dashboard.doctor_dashboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class APIException extends RuntimeException {
    public APIException(String message) {
        super(message);
    }
}
