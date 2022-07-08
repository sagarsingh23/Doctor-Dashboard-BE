package com.dashboard.doctor_dashboard.entities.dtos;

import com.dashboard.doctor_dashboard.entities.model.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.model.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentDto {
    private Long appointId;
    private String category;
    private LocalDate dateOfAppointment;
    private String symptoms;
    private String patientName;
    private String patientEmail;
    private String doctorName;
    private LocalTime appointmentTime;
    private Boolean isRead;
    private String status;
    private Boolean isBookedAgain;
    private Long followUpAppointmentId;
    private Patient patient;
    private DoctorDetails doctorDetails;
}
