package com.dashboard.doctor_dashboard.services.appointment_service;

import com.dashboard.doctor_dashboard.entities.Appointment;
//import com.dashboard.doctor_dashboard.entities.dtos.AppointmentListDto;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorAppointmentListDto;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientAppointmentListDto;
import com.dashboard.doctor_dashboard.entities.dtos.PatientProfileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface AppointmentService {

    Appointment addAppointment(Appointment appointment, HttpServletRequest request);
    ResponseEntity<GenericMessage> getAllAppointmentByPatientId(Long patientId);
    List<DoctorAppointmentListDto> getAllAppointmentByDoctorId(Long doctorId);
    PatientProfileDto getAppointmentById(Long appointId);
    ResponseEntity<GenericMessage> recentAppointment(Long doctorId);

    ResponseEntity<GenericMessage> weeklyPatientCountChart(Long doctorId);


    ResponseEntity<GenericMessage> totalNoOfAppointment(Long doctorId);
    ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(Long doctorId);

}
