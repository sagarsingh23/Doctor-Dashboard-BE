package com.dashboard.doctor_dashboard.exception;

import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
public class ValidationsSchema {
    private Date timestamp;
    private List<String> messages;
    private String details;

}
