package com.dashboard.doctor_dashboard.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationsException extends RuntimeException {
    private List<String> messages;

}
