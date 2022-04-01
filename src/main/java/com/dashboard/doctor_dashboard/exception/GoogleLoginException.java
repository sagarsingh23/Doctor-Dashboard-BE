package com.dashboard.doctor_dashboard.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginException extends RuntimeException {
    private String message;

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
