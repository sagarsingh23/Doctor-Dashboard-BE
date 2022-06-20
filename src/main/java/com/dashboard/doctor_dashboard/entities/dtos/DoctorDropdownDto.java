package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DoctorDropdownDto {

    private long id;

    private String speciality;

    private String name;

    private String emailId;

    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
