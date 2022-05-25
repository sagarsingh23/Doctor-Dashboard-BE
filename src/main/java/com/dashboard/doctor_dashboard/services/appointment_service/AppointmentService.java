package com.dashboard.doctor_dashboard.services.appointment_service;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.AppointmentListDto;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface AppointmentService {

    Appointment addAppointment(Appointment appointment, HttpServletRequest request);
    List<AppointmentListDto> getAllAppointmentByPatientId(Long patientId);
    List<AppointmentListDto> getAllAppointmentByDoctorId(Long doctorId);
    Appointment getAppointmentById(Long appointId);

    ResponseEntity<GenericMessage> weeklyPatientCountChart(Long doctorId);


    }
