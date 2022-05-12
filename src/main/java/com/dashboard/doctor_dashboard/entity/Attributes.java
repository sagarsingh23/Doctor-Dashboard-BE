package com.dashboard.doctor_dashboard.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@NoArgsConstructor
@Entity
@Table(
        name = "attributes"
)
public class Attributes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aID;
    private Long bloodPressure;

    @NotEmpty
    @Pattern(regexp = "^(O-|O[+]|A-|B-|A[+]|AB-|B[+]|AB[+])", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String bloodGroup;

    private Long glucoseLevel;
    private Double bodyTemp;
    private String notes;

    @NotEmpty
    @Size(max = 100)
    private String symptoms;


    @JsonBackReference
    @OneToOne()
    @JoinColumn(name = "id")
    private Patient patient;


    public Long getAID() {
        return aID;
    }

    public void setAID(Long aID) {
        this.aID = aID;
    }

    public Long getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(Long bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
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

    public String getNotes() {
        return notes;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
