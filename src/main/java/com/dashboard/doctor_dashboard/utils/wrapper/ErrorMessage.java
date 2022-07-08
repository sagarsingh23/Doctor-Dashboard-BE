package com.dashboard.doctor_dashboard.utils.wrapper;

import lombok.Data;

@Data
public class ErrorMessage {
    private String errorStatus;
    private Object errorData;
}
