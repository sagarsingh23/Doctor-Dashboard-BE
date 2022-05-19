package com.dashboard.doctor_dashboard.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class APIException extends RuntimeException {

    public final HttpStatus status;
    public final String message;

    public APIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
