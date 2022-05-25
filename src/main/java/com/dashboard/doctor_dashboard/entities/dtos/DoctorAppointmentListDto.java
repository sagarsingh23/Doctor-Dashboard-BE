package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public class DoctorAppointmentListDto {

    private Long appointId;
    private LocalDate dateOfAppointment;
    private String patientName;
    private String patientEmail;

    public Long getAppointId() {
        return appointId;
    }

    public void setAppointId(Long appointId) {
        this.appointId = appointId;
    }

    public LocalDate getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(LocalDate dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
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

}

