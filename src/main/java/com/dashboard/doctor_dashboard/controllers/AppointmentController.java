package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.AppointmentListDto;
import com.dashboard.doctor_dashboard.services.appointment_service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/appointment")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public Appointment addAppointment(@RequestBody Appointment appointment) {
        return appointmentService.addAppointment(appointment);
    }

    @GetMapping("/getAllAppointments/patient/{patientId}")
    public List<AppointmentListDto> getAllAppointmentByPatientId(@PathVariable("patientId") Long patientId) {
        return appointmentService.getAllAppointmentByPatientId(patientId);
    }

    @GetMapping("/getAllAppointments/doctor/{doctorId}")
    public List<AppointmentListDto> getAllAppointmentByDoctorId(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.getAllAppointmentByDoctorId(doctorId);
    }

    @GetMapping("/{appointId}")
    public Appointment getAppointmentById(@PathVariable("appointId") Long appointId) {
        return appointmentService.getAppointmentById(appointId);
    }
}
