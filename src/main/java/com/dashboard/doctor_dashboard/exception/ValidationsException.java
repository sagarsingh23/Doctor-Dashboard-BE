package com.dashboard.doctor_dashboard.exception;

import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
public class ValidationsException extends RuntimeException {
    private List<String> messages;

    public List<String> getMessages() {
        return messages;
    }

}
