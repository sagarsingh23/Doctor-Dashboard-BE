package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.Data;

@Data
public class GenericMessage {
    private String status;
    private Object data;
}
