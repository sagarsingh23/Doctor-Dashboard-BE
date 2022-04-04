package com.dashboard.doctor_dashboard.exception;

public class ReportNotFound extends RuntimeException{

    public ReportNotFound(String message){
        super(String.format(message));
    }
}
