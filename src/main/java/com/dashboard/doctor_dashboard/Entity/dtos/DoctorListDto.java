package com.dashboard.doctor_dashboard.Entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorListDto {
    private long id;
    private String name;
    private String email;

}
