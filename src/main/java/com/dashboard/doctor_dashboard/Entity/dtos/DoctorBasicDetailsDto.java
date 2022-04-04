package com.dashboard.doctor_dashboard.Entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorBasicDetailsDto {
    private String firstName;
    private String email;
    private String speciality;
    private String phoneNo;
    private String gender;
    private Short age;

}
