package com.dashboard.doctor_dashboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class MailErrorException extends RuntimeException {
    public MailErrorException(String message) {
        super(message);
    }
}
