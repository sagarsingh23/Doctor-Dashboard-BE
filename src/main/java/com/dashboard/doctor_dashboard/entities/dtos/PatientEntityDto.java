package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientEntityDto {

    private Long pID;
    private String mobileNo;
    private String gender;
    private int age;
    private String bloodGroup;
    private String address;
    private String alternateMobileNo;

}
