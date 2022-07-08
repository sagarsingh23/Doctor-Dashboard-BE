package com.dashboard.doctor_dashboard.entities.dtos;

import com.dashboard.doctor_dashboard.entities.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttributesDto {
    private Long aID;
    private String  bloodPressure;
    private Long glucoseLevel;
    private Double bodyTemp;
    private String prescription;
    private Appointment appointment;
}
