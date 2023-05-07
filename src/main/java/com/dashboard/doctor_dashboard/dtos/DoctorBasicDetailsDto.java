package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.enums.Category;
import com.dashboard.doctor_dashboard.enums.Gender;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class DoctorBasicDetailsDto {
    private String firstName;
    private String email;
    private Category speciality;
    private String phoneNo;
    private Gender gender;
    private Short age;
    private String degree;
    private short exp;


    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public Category getSpeciality() {
        return speciality;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public Gender getGender() {
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
