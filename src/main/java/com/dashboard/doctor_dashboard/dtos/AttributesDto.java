package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.entities.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttributesDto {
    @NotNull
    @NotEmpty(message = "Blood pressure can't be empty")
    private String  bloodPressure;

    @NotNull(message = "Glucose level can't be null")
    private Long glucoseLevel;

    @NotNull(message = "Body temperature can't be null")
    private Double bodyTemp;

    @NotNull
    @NotEmpty(message = "Notes can't be empty")
    private String prescription;

    @NotNull(message = "Appointment details can't be empty")
    private Appointment appointment;
}
