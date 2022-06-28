package com.dashboard.doctor_dashboard.services.appointment_service;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientProfileDto;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface AppointmentService {

    ResponseEntity<GenericMessage>  addAppointment(Appointment appointment, HttpServletRequest request) throws MessagingException, JSONException, UnsupportedEncodingException;
    ResponseEntity<GenericMessage> getAllAppointmentByPatientId(Long patientId);
    ResponseEntity<GenericMessage> getAllAppointmentByDoctorId(Long doctorId);
    ResponseEntity<GenericMessage> getFollowDetails(Long appointId);


    PatientProfileDto getAppointmentById(Long appointId);
    ResponseEntity<GenericMessage> recentAppointment(Long doctorId);

    ResponseEntity<GenericMessage> weeklyDoctorCountChart(Long doctorId);
    ResponseEntity<GenericMessage> weeklyPatientCountChart(Long doctorId);


    Map<Long, Map<LocalDate,List<Boolean>>> returnMap();
    ResponseEntity<GenericMessage> totalNoOfAppointment(Long doctorId);
    ResponseEntity<GenericMessage> todayAppointments(Long doctorId);
    ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(Long doctorId);
    ResponseEntity<GenericMessage> patientCategoryGraph(Long patientId);



    List<Boolean> checkSlots(LocalDate date, Long doctorId);


}
