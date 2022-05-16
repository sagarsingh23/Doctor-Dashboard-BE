package com.dashboard.doctor_dashboard.entities.dtos;

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

    public String getSpeciality() {
        return speciality;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public Short getAge() {
        return age;
    }
}
