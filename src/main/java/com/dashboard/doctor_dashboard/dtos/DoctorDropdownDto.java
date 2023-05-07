package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.enums.Category;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DoctorDropdownDto {

    private long id;
    private String name;
    private String emailId;
    private Category speciality;





    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Category speciality) {
        this.speciality = speciality;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
