package com.dashboard.doctor_dashboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidDate extends RuntimeException{
    public InvalidDate(String message) {
        super(message);
    }
}
