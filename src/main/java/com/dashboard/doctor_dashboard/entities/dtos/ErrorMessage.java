package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.Data;

@Data
public class ErrorMessage {
    private String errorStatus;
    private Object errorData;
}
