package com.dashboard.doctor_dashboard.entities.dtos;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.Prescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentViewDto {
    private String name;
    private String email;
    private String gender;
    private String speciality;

    private short age;
    private String bloodGroup;
    private LocalDate dateOfAppointment;
    private LocalTime timeOfAppointment;
    private Attributes attributes;
    private String status;
    private List<Prescription> prescription;

    public AppointmentViewDto(String name, String speciality, LocalDate dateOfAppointment, LocalTime timeOfAppointment, String status,String bloodGroup,short age,String gender) {
        this.name = name;
        this.speciality = speciality;
        this.dateOfAppointment = dateOfAppointment;
        this.timeOfAppointment = timeOfAppointment;
        this.status = status;
        this.bloodGroup=bloodGroup;
        this.gender=gender;
        this.age=age;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPrescription(List<Prescription> prescription) {
        this.prescription = prescription;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getSpeciality() {
        return speciality;
    }

    public LocalDate getDateOfAppointment() {
        return dateOfAppointment;
    }

    public LocalTime getTimeOfAppointment() {
        return timeOfAppointment;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public String getStatus() {
        return status;
    }

    public List<Prescription> getPrescription() {
        return prescription;
    }




}

