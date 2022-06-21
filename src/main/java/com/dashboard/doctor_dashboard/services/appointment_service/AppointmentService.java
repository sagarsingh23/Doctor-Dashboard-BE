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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface AppointmentService {

    ResponseEntity<GenericMessage>  addAppointment(Appointment appointment, HttpServletRequest request);
    ResponseEntity<GenericMessage> getAllAppointmentByPatientId(Long patientId);
    ResponseEntity<GenericMessage> getPastAppointmentByDoctorId(Long doctorId);
    ResponseEntity<GenericMessage> getTodayAppointmentByDoctorId(Long doctorId);
    ResponseEntity<GenericMessage> getUpcomingAppointmentByDoctorId(Long doctorId);
    ResponseEntity<GenericMessage> getFollowDetails(Long appointId);


    PatientProfileDto getAppointmentById(Long appointId);
    ResponseEntity<GenericMessage> recentAppointment(Long doctorId);

    ResponseEntity<GenericMessage> weeklyPatientCountChart(Long doctorId);

    Map<Long, Map<LocalDate,List<Boolean>>> returnMap();
    ResponseEntity<GenericMessage> totalNoOfAppointment(Long doctorId);
    ResponseEntity<GenericMessage> todayAppointments(Long doctorId);
    ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(Long doctorId);
    ResponseEntity<GenericMessage> patientCategoryGraph(Long patientId);



    List<Boolean> checkSlots(LocalDate date, Long doctorId);


}
