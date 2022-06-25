package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.Patient;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientDetailsUpdateDto;
import com.dashboard.doctor_dashboard.entities.dtos.PatientEntityDto;
import com.dashboard.doctor_dashboard.services.appointment_service.AppointmentService;
import com.dashboard.doctor_dashboard.services.patient_service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/patient")
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

//    @GetMapping("/doctor/{doctorId}")
//    public ResponseEntity<GenericMessage> getAllPatientsByDoctorId(@PathVariable("doctorId") Long doctorId) {
//        return patientService.getAllPatientByDoctorId(doctorId);
//    }

    @GetMapping("/{id}/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> getPatientDtoById(@PathVariable("id") Long id, @PathVariable("doctorId") Long doctorId) {
        return patientService.getPatientById(id, doctorId);
    }

    @GetMapping("/{patientId}/appointment/{appointmentId}")
    public ResponseEntity<GenericMessage> getAppointmentViewByAppointmentId(@PathVariable("patientId") long patientId, @PathVariable("appointmentId") long appointmentId){
        return patientService.viewAppointment(appointmentId,patientId);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<GenericMessage> updatePatient(@PathVariable("id") Long id, @RequestBody Patient patient) {
//        return patientService.updatePatient(id, patient);
//    }
    @GetMapping("/patientProfile/{loginId}")
    public ResponseEntity<GenericMessage> patientProfile(@PathVariable("loginId") Long loginId){
        return patientService.getPatientDetailsById(loginId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericMessage> updatePatient(@PathVariable("id") Long id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericMessage> updatePatientDetails(@PathVariable("id") Long id, @RequestBody PatientDetailsUpdateDto patientDetailsUpdateDto) {
        return patientService.updatePatientDetails(id, patientDetailsUpdateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deletePatientById(@PathVariable("id") Long id) {
        return patientService.deletePatientById(id);
    }
//
//    @PutMapping("/changeStatus/{id}")
//    public ResponseEntity<GenericMessage> changePatientStatus(@PathVariable("id") Long id, @Valid @RequestBody StatusDto status) {
//        return patientService.changePatientStatus(id, status.getStatus());
//    }

//    @GetMapping("/recentAdded/doctor/{doctorId}")
//    public ResponseEntity<GenericMessage> recentlyAddedPatient(@PathVariable("doctorId") Long doctorId) {
//        return patientService.recentlyAddedPatient(doctorId);
//    }


    //Dashboard Chart


    @GetMapping("/{doctorId}/totalPatient")
    public ResponseEntity<GenericMessage> totalPatient(@PathVariable("doctorId") Long doctorId) {
        return patientService.totalNoOfPatient(doctorId);
    }

    @GetMapping("/{doctorId}/totalActivePatient")
    public ResponseEntity<GenericMessage> totalNoOfPatientAddedThisWeek(@PathVariable("doctorId") Long doctorId) {
        return patientService.totalNoOfPatientAddedThisWeek(doctorId);
    }


    @GetMapping("/{doctorId}/category")
    public ResponseEntity<GenericMessage> patientCategory(@PathVariable("doctorId") Long doctorId) {
        return patientService.patientCategory(doctorId);
    }

    @GetMapping("/{doctorId}/gender")
    public ResponseEntity<GenericMessage> gender(@PathVariable("doctorId") Long doctorId) {
        return patientService.gender(doctorId);
    }

    @GetMapping("/{doctorId}/activePatient")
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(@PathVariable("doctorId") Long doctorId) {
        return patientService.weeklyPatientCountChart(doctorId);
    }

    @GetMapping("/{doctorId}/bloodGroup")
    public ResponseEntity<GenericMessage> bloodGroup(@PathVariable("doctorId") Long doctorId) {
        return patientService.bloodGroup(doctorId);
    }

    @GetMapping("/{doctorId}/ageChart")
    public ResponseEntity<GenericMessage> ageChart(@PathVariable("doctorId") Long doctorId) {
        return patientService.ageChart(doctorId);
    }
    //{
//        bloodGroup:{},
//        ageChart:{},

//    }

    //Add-On feature Refer Patient

    @PutMapping("/{id}/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> referPatients(@PathVariable("doctorId") Long doctorId, @PathVariable("id") Long patientId) {

        return patientService.referPatients(doctorId, patientId);
    }

    @GetMapping("/message/{doctorId}")
    public ResponseEntity<GenericMessage> getMessageForReferredPatient(@PathVariable("doctorId") Long doctorId) {
        return patientService.getMessageForReferredPatient(doctorId);
    }

    @PutMapping("/changeMessage/{doctorId}")
    public ResponseEntity<GenericMessage> changeStatus(@PathVariable("doctorId") Long doctorId) {
        return patientService.changeStatus(doctorId);
    }

    @GetMapping("/{patientId}/getNotifications")
    public ResponseEntity<GenericMessage> getNotifications(@PathVariable("patientId") Long patientId){

        return patientService.getNotifications(patientId);
    }

}
