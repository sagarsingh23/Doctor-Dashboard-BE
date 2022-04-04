package com.dashboard.doctor_dashboard.Entity.dtos;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class StatusDto {

    @Pattern(regexp = "^(active|inactive)",flags = Pattern.Flag.CASE_INSENSITIVE,
    message = "select from specified status")
    private String status;
}
