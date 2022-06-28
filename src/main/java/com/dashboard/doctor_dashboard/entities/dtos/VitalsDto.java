package com.dashboard.doctor_dashboard.entities.dtos;

import com.dashboard.doctor_dashboard.entities.Appointment;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class VitalsDto {

    @NotNull
    private Long appointmentId;
    @NotNull
    private Long glucoseLevels;

    @NotNull
    private Double bodyTemperature;

    @NotNull
    @NotEmpty
    private String bloodPressure;

    public Long getGlucoseLevels() {
        return glucoseLevels;
    }

    public Double getBodyTemperature() {
        return bodyTemperature;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }


    public String getBloodPressure() {
        return bloodPressure;
    }

}
