package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.services.PdFGeneratorServiceImpl;
import com.dashboard.doctor_dashboard.services.appointment_service.AppointmentService;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/appointment")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PdFGeneratorServiceImpl pdFGeneratorService;

    @PostMapping("/patient")
    public ResponseEntity<GenericMessage> addAppointment(@RequestBody Appointment appointment, HttpServletRequest request) throws MessagingException, JSONException, UnsupportedEncodingException {

        return appointmentService.addAppointment(appointment,request);

    }
    @GetMapping("/getMap")
    public Map<Long, Map<LocalDate, List<Boolean>>> getMap(){
        return appointmentService.returnMap();
    }
    @GetMapping("/getAvailableSlots/{doctorId}/{date}")
    public ResponseEntity<GenericMessage> showAvailableSlots(@PathVariable String  date,@PathVariable("doctorId") Long doctorId){
        System.out.println(LocalDate.parse(date));
         return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,appointmentService.checkSlots(LocalDate.parse(date),doctorId)),HttpStatus.OK);
    }
    @GetMapping("/getAllAppointments/patient/{patientId}")
    public ResponseEntity<GenericMessage> getAllAppointmentByPatientId(@PathVariable("patientId") Long patientId) {
        return appointmentService.getAllAppointmentByPatientId(patientId) ;
    }

    @GetMapping("/getAllAppointments/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> getAllAppointmentByDoctorId(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.getAllAppointmentByDoctorId(doctorId) ;
    }

//    @GetMapping("/getAllAppointments/doctor/{doctorId}")
//    public ResponseEntity<GenericMessage> getAllAppointmentByDoctorId(@PathVariable("doctorId") Long doctorId) {
//        GenericMessage genericMessage = new GenericMessage();
//
//        genericMessage.setData(appointmentService.getAllAppointmentByDoctorId(doctorId));
//        genericMessage.setStatus(Constants.SUCCESS);
//        return new ResponseEntity<>(genericMessage,HttpStatus.OK);
//    }


//    @GetMapping("/getAllAppointments/past/doctor/{doctorId}")
//    public ResponseEntity<GenericMessage> getPastAppointmentByDoctorId(@PathVariable("doctorId") Long doctorId) {
//        return appointmentService.getPastAppointmentByDoctorId(doctorId);
//    }
//
//    @GetMapping("/getAllAppointments/today/doctor/{doctorId}")
//    public ResponseEntity<GenericMessage> getTodayAppointmentByDoctorId(@PathVariable("doctorId") Long doctorId) {
//        return appointmentService.getTodayAppointmentByDoctorId(doctorId);
//    }
//
//
//    @GetMapping("/getAllAppointments/upcoming/doctor/{doctorId}")
//    public ResponseEntity<GenericMessage> getUpcomingAppointmentByDoctorId(@PathVariable("doctorId") Long doctorId) {
//        return appointmentService.getUpcomingAppointmentByDoctorId(doctorId);
//    }
//


    @GetMapping("/{appointId}/patient")
    public ResponseEntity<GenericMessage> getAppointmentById(@PathVariable("appointId") Long appointId) {
        GenericMessage genericMessage = new GenericMessage();

        genericMessage.setData(appointmentService.getAppointmentById(appointId));
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage,HttpStatus.OK);
    }

    @GetMapping("chart/{doctorId}/weeklyGraphDoctor")
    public ResponseEntity<GenericMessage> weeklyDoctorCountChart(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.weeklyDoctorCountChart(doctorId);
    }

    @GetMapping("chart/{patientId}/weeklyGraphPatient")
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(@PathVariable("patientId") Long patientId) {
        return appointmentService.weeklyPatientCountChart(patientId);
    }


    @GetMapping("/recentAdded/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> recentAppointment(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.recentAppointment(doctorId);
    }




    @GetMapping("/chart/{doctorId}/totalPatient")
    public ResponseEntity<GenericMessage> totalNoOfAppointment(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.totalNoOfAppointment(doctorId);
    }

    @GetMapping("/chart/{doctorId}/todayAppointments")
    public ResponseEntity<GenericMessage> todayAppointments(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.todayAppointments(doctorId);
    }

    @GetMapping("/chart/{doctorId}/totalActivePatient")
    public ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.totalNoOfAppointmentAddedThisWeek(doctorId);
    }

    @GetMapping("/{loginId}/category")
    public ResponseEntity<GenericMessage> patientCategoryGraph(@PathVariable("loginId") Long loginId) {
        return appointmentService.patientCategoryGraph(loginId);
    }

    @GetMapping("/{appointId}")
    public ResponseEntity<GenericMessage>getFollowDetails(@PathVariable("appointId") Long appointId){
        return appointmentService.getFollowDetails(appointId);
    }


}
