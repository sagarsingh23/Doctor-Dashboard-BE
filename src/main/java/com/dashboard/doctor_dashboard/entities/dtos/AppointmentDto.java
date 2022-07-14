package com.dashboard.doctor_dashboard.entities.dtos;

import com.dashboard.doctor_dashboard.entities.model.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.model.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentDto {
    private Long appointId;

    @Pattern(regexp = "^((?i)Orthologist|Dentist|General|Gastrologist|Dermatologist)", message = "Select from specified speciality [Orthologist,Dentist,Dermatologist,General,Gastrologist]")
    private String category;

    @NotNull
    @Future(message = "Only future dates can be entered ")
    private LocalDate dateOfAppointment;

    @NotNull
    @NotEmpty
    @Size(max = 100,message = "Symptoms can't be exceed 100 character")
    private String symptoms;

    @NotNull
    @NotEmpty(message = "Patient name can't be empty")
    private String patientName;

    @NotNull
    @NotEmpty(message = "Patient email can't be empty")
    private String patientEmail;

    @NotNull
    @NotEmpty(message = "Doctor name can't be empty")
    private String doctorName;

    @NotNull(message = "Time can't be null")
    private LocalTime appointmentTime;

    private Boolean isRead;

    @NotNull
    @NotEmpty(message = "Status can't be empty")
    private String status;

    private Boolean isBookedAgain;

    private Long followUpAppointmentId;

    @NotNull(message = "Patient details can't be null")
    private Patient patient;

    @NotNull(message = "doctor details can't be null")
    private DoctorDetails doctorDetails;
}
