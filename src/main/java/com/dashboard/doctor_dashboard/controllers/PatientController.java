package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.Utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientDetailsUpdateDto;
import com.dashboard.doctor_dashboard.entities.dtos.PatientEntityDto;
import com.dashboard.doctor_dashboard.services.appointment_service.AppointmentService;
import com.dashboard.doctor_dashboard.services.patient_service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/patient")
public class PatientController {


    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    //CRUD operation for patient

    @PostMapping("/{loginId}")
    public ResponseEntity<GenericMessage> addPatientDetails(@RequestBody PatientEntityDto patient,@PathVariable("loginId") Long loginId) {
        return patientService.addPatient(patient,loginId);
    }

    @GetMapping("/{patientId}/appointment/{appointmentId}")
    public ResponseEntity<GenericMessage> getAppointmentViewByAppointmentId(@PathVariable("patientId") long patientId, @PathVariable("appointmentId") long appointmentId){
        return patientService.viewAppointment(appointmentId,patientId);
    }

    @GetMapping("/patientProfile/{loginId}")
    public ResponseEntity<GenericMessage> patientProfile(@PathVariable("loginId") Long loginId){
        return patientService.getPatientDetailsById(loginId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericMessage> updatePatientDetails(@PathVariable("id") Long id, @RequestBody PatientDetailsUpdateDto patientDetailsUpdateDto) {
        return patientService.updatePatientDetails(id, patientDetailsUpdateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deletePatientById(@PathVariable("id") Long id) {
        return patientService.deletePatientById(id);
    }

    @GetMapping("/{patientId}/getNotifications")
    public ResponseEntity<GenericMessage> getNotifications(@PathVariable("patientId") Long patientId){
        return patientService.getNotifications(patientId);
    }

}
