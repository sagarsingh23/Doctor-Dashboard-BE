package com.dashboard.doctor_dashboard.services.appointment_service;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.AppointmentListDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppointmentService {

    Appointment addAppointment(Appointment appointment);
    List<AppointmentListDto> getAllAppointmentByPatientId(Long patientId);
    List<AppointmentListDto> getAllAppointmentByDoctorId(Long doctorId);
    Appointment getAppointmentById(Long appointId);


}
