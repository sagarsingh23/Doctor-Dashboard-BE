package com.dashboard.doctor_dashboard.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(
        name = "attributes"
)
public class Attributes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aID;
    private String  bloodPressure;
    private Long glucoseLevel;
    private Double bodyTemp;
    private String prescription;


    @JsonBackReference
    @OneToOne()
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;


    public Long getAID() {
        return aID;
    }

    public void setAID(Long aID) {
        this.aID = aID;
    }

    public String  getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String  bloodPressure) {
        this.bloodPressure = bloodPressure;
    }


    public Long getGlucoseLevel() {
        return glucoseLevel;
    }

    public void setGlucoseLevel(Long glucoseLevel) {
        this.glucoseLevel = glucoseLevel;
    }

    public Double getBodyTemp() {
        return bodyTemp;
    }

    public void setBodyTemp(Double bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }


}
