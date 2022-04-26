package com.dashboard.doctor_dashboard.Entity.dtos;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class DoctorListDto {
    private long id;
    private String name;
    private String email;


    public String getName() {
        return name;
    }

}
