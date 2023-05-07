package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.Prescription;
import com.dashboard.doctor_dashboard.enums.BloodGroup;
import com.dashboard.doctor_dashboard.enums.Category;
import com.dashboard.doctor_dashboard.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentViewDto {
    @NotNull
    @NotEmpty(message = "name is empty")
    private String name;
    @NotNull
    @NotEmpty
    private String email;
    @NotNull
    @NotEmpty
    private Gender gender;
    @NotNull
    @NotEmpty
    private Category speciality;

    @NotNull
    private short age;

    @NotNull
    @NotEmpty
    private BloodGroup bloodGroup;

    @NotNull
    @Future(message = "Only future dates can be entered ")
    private LocalDate dateOfAppointment;

    @NotNull
    private LocalTime timeOfAppointment;

    @NotNull
    private Attributes attributes;

    @NotNull
    @NotEmpty
    private String status;

    @NotNull
    private List<Prescription> prescription;

    @SuppressWarnings("squid:S107")
    public AppointmentViewDto(String name, Category speciality, LocalDate dateOfAppointment, LocalTime timeOfAppointment, String status,BloodGroup bloodGroup,short age,Gender gender) {
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

    public void setGender(Gender gender) {
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

    public Gender getGender() {
        return gender;
    }

    public Category getSpeciality() {
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

