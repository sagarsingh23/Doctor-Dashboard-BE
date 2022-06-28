package com.dashboard.doctor_dashboard.entities.dtos;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.Prescription;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileDto {

    private Long appointId;
    private LocalDate dateOfAppointment;
    private String patientName;
    private String patientEmail;
    private String symptoms;
    private String category;

    private Boolean isBookedAgain;

    public Boolean getBookedAgain() {
        return isBookedAgain;
    }

    public void setBookedAgain(Boolean bookedAgain) {
        isBookedAgain = bookedAgain;
    }

    public Long getFollowUpAppointmentId() {
        return followUpAppointmentId;
    }

    public void setFollowUpAppointmentId(Long followUpAppointmentId) {
        this.followUpAppointmentId = followUpAppointmentId;
    }

    private Long followUpAppointmentId;
    private Attributes attributes;
    private PatientEntityDto patient;
    private List<Prescription> prescription;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public PatientEntityDto getPatient() {
        return patient;
    }

    public void setPatient(PatientEntityDto patient) {
        this.patient = patient;
    }

    public List<Prescription> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<Prescription> prescription) {
        this.prescription = prescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

