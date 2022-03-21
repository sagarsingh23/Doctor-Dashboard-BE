package com.dashboard.doctor_dashboard.Entity.dtos;

import lombok.Data;

@Data
public class PatientListDto {
    private Long pID;
    private String fullName;
    private String emailId;
    private int age;
    private String status;

}
