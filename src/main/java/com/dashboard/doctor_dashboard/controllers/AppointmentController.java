package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.services.appointment_service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("api/appointment")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {
// new
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/patient")
    public ResponseEntity<GenericMessage> addAppointment(@RequestBody Appointment appointment, HttpServletRequest request) {
        GenericMessage genericMessage = new GenericMessage();

        genericMessage.setData(appointmentService.addAppointment(appointment,request));
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage,HttpStatus.OK);
    }

    @GetMapping("/getAllAppointments/patient/{patientId}")
    public ResponseEntity<GenericMessage> getAllAppointmentByPatientId(@PathVariable("patientId") Long patientId) {
        return appointmentService.getAllAppointmentByPatientId(patientId);
    }

    @GetMapping("/getAllAppointments/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> getAllAppointmentByDoctorId(@PathVariable("doctorId") Long doctorId) {
        GenericMessage genericMessage = new GenericMessage();

        genericMessage.setData(appointmentService.getAllAppointmentByDoctorId(doctorId));
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage,HttpStatus.OK);
    }

    @GetMapping("/{appointId}/patient")
    public ResponseEntity<GenericMessage> getAppointmentById(@PathVariable("appointId") Long appointId) {
        GenericMessage genericMessage = new GenericMessage();

        genericMessage.setData(appointmentService.getAppointmentById(appointId));
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage,HttpStatus.OK);
    }

    @GetMapping("chart/{doctorId}/activePatient")
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.weeklyPatientCountChart(doctorId);
    }
    @GetMapping("/recentAdded/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> recentAppointment(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.recentAppointment(doctorId);
    }




    @GetMapping("/chart/{doctorId}/totalPatient")
    public ResponseEntity<GenericMessage> totalNoOfAppointment(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.totalNoOfAppointment(doctorId);
    }

    @GetMapping("/chart/{doctorId}/totalActivePatient")
    public ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.totalNoOfAppointmentAddedThisWeek(doctorId);
    }



    //new

}
