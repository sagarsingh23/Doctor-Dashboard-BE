package com.dashboard.doctor_dashboard.exceptions;

public class ReportNotFound extends RuntimeException {

    public ReportNotFound(String message) {
        super(String.format(message));
    }
}
