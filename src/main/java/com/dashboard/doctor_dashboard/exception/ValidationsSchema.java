package com.dashboard.doctor_dashboard.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ValidationsSchema {
    private Date timestamp;
    private List<String> messages;
    private String details;

}
