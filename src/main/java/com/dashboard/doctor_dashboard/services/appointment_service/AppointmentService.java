package com.dashboard.doctor_dashboard.services.appointment_service;

import com.dashboard.doctor_dashboard.entities.dtos.AppointmentDto;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface AppointmentService {

    ResponseEntity<GenericMessage>  addAppointment(AppointmentDto appointment, HttpServletRequest request) throws MessagingException, JSONException, UnsupportedEncodingException;
    ResponseEntity<GenericMessage> getAllAppointmentByPatientId(Long patientId,int pageNo);
    ResponseEntity<GenericMessage> getAllAppointmentByDoctorId(Long doctorId,int pageNo);
    ResponseEntity<GenericMessage> getFollowDetails(Long appointId);


    ResponseEntity<GenericMessage> getAppointmentById(Long appointId);
    ResponseEntity<GenericMessage> recentAppointment(Long doctorId);

    ResponseEntity<GenericMessage> weeklyDoctorCountChart(Long doctorId);
    ResponseEntity<GenericMessage> weeklyPatientCountChart(Long doctorId);


    ResponseEntity<GenericMessage> totalNoOfAppointment(Long doctorId);
    ResponseEntity<GenericMessage> todayAppointments(Long doctorId);
    ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(Long doctorId);
    ResponseEntity<GenericMessage> patientCategoryGraph(Long patientId);



    List<Boolean> checkSlots(LocalDate date, Long doctorId);


}
