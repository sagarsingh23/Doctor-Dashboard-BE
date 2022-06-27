package com.dashboard.doctor_dashboard.entities.dtos;

import com.dashboard.doctor_dashboard.entities.Appointment;
import lombok.AllArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class VitalsDto {

    @NotNull
    private Long glucoseLevel;

    @NotNull
    private Double bodyTemp;

    @NotNull
    @NotEmpty
    private String bloodPressure;

    @OneToOne()
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;


    public Long getGlucoseLevel() {
        return glucoseLevel;
    }

    public Double getBodyTemp() {
        return bodyTemp;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
