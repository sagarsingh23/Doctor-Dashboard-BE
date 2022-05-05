package com.dashboard.doctor_dashboard.entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class DoctorListDto {
    private long id;

    private String name;
    private String email;


    public String getName() {
        return name;
    }
    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

}
