package com.dashboard.doctor_dashboard.entity.dtos;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class DoctorBasicDetailsDto {
    private String firstName;
    private String email;
    private String speciality;
    private String phoneNo;
    private String gender;
    private Short age;


    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

}
