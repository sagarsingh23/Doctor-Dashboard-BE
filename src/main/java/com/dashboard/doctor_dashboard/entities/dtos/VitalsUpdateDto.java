package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VitalsUpdateDto {

    private String patientName;

    private String patientMail;

    private Short glucose;

    private Short temperature;

    private String bloodPressure;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientMail() {
        return patientMail;
    }

    public void setPatientMail(String patientMail) {
        this.patientMail = patientMail;
    }

    public Short getGlucose() {
        return glucose;
    }

    public void setGlucose(Short glucose) {
        this.glucose = glucose;
    }

    public Short getTemperature() {
        return temperature;
    }

    public void setTemperature(Short temperature) {
        this.temperature = temperature;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }
}
