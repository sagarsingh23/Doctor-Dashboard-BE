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
    private String degree;
    private short exp;


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

    public String getDegree() {
        return degree;
    }

    public short getExp() {
        return exp;
    }
}
