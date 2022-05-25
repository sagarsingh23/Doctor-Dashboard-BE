package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public class PatientAppointmentListDto {
    private Long appointId;
    private String category;
    private LocalDate dateOfAppointment;
    private String doctorName;

    public Long getAppointId() {
        return appointId;
    }

    public void setAppointId(Long appointId) {
        this.appointId = appointId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(LocalDate dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
