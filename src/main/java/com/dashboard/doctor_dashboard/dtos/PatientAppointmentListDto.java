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
public class PatientAppointmentListDto {
    private Long appointId;
    private String category;
    private LocalDate dateOfAppointment;
    private LocalTime appointmentTime;
    private String doctorName;
    private String status;
    private Boolean isBookedAgain;
}
