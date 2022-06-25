package com.dashboard.doctor_dashboard.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidDate extends RuntimeException{

    String date;
    String message;

    @Override
    public String toString() {
        return "InvalidDate{" +
                "date=" + date +
                ", message='" + message + '\'' +
                '}';
    }
}
