package com.dashboard.doctor_dashboard.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@Getter

public class ValidationsSchema {
    private Date timestamp;
    private List<String> messages;
    private String details;

}
