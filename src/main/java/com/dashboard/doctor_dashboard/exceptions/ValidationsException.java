package com.dashboard.doctor_dashboard.exceptions;

import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
public class ValidationsException extends RuntimeException {
    private final List<String>  messages;

    public List<String> getMessages() {
        return messages;
    }

}
