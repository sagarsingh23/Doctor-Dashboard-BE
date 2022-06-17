package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DoctorDropdownDto {

    private long id;

    private String specialisation;

    private String name;

    private String email;

    public long getId() {
        return id;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
