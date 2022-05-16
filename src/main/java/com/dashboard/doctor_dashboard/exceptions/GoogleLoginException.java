package com.dashboard.doctor_dashboard.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class GoogleLoginException extends RuntimeException {
    private final String message;

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
