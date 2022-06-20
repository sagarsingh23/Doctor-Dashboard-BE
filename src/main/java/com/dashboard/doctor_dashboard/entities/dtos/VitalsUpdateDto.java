package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VitalsUpdateDto {

    private Long appointmentId;
    private Long glucoseLevels;

    private Double bodyTempareture;

    private String bloodPressure;

    public Long getGlucoseLevels() {
        return glucoseLevels;
    }

    public Double getBodyTempareture() {
        return bodyTempareture;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }


    public String getBloodPressure() {
        return bloodPressure;
    }
}
