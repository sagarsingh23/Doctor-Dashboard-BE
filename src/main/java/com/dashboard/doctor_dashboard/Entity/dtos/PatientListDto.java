package com.dashboard.doctor_dashboard.Entity.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientListDto {
    private Long pID;
    private String fullName;
    private String emailId;
    private String status;
    private String category;
    private LocalDate lastVisitedDate;
    private String mobileNo;
    private String gender;
    private int age;

}
