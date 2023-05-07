package com.dashboard.doctor_dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DoctorAppointmentListDto {

    private Long appointId;
    private LocalDate dateOfAppointment;
    private String patientName;
    private String patientEmail;
    private String status;
    private LocalTime appointmentTime;

}

