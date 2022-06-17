package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;

import java.time.LocalTime;
@AllArgsConstructor
public class PatientViewDto {

    private Long id;



    private LocalTime timeOfAppointment;

    private String patientName;

    private String patientEmail;

    private String patientStatus;

    public Long getId() {
        return id;
    }

    public LocalTime getTimeOfAppointment() {
        return timeOfAppointment;
    }

    public void setTimeOfAppointment(LocalTime timeOfAppointment) {
        this.timeOfAppointment = timeOfAppointment;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
    }
}
