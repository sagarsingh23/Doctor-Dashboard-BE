package com.dashboard.doctor_dashboard.controller;

import com.dashboard.doctor_dashboard.entity.Patient;
import com.dashboard.doctor_dashboard.entity.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entity.dtos.PatientListDto;
import com.dashboard.doctor_dashboard.entity.dtos.StatusDto;
import com.dashboard.doctor_dashboard.service.patient_service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/patient")
public class PatientController {


    @Autowired
    private PatientService patientService;

    //CRUD operation for patient

    @PostMapping()
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<PatientListDto> getAllPatientsByDoctorId(@PathVariable("doctorId") Long doctorId) {
        return patientService.getAllPatientByDoctorId(doctorId);
    }

    @GetMapping("/{id}/doctor/{doctorId}")
    public PatientDto getPatientDtoById(@PathVariable("id") Long id, @PathVariable("doctorId") Long doctorId) {
        return patientService.getPatientById(id, doctorId);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable("id") Long id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);
    }

    @DeleteMapping("/{id}")
    public String deletePatientById(@PathVariable("id") Long id) {
        patientService.deletePatientById(id);
        return "Successfully Deleted";
    }

    @PutMapping("/changeStatus/{id}")
    public String changePatientStatus(@PathVariable("id") Long id, @Valid @RequestBody StatusDto status) {
        patientService.changePatientStatus(id, status.getStatus());
        return "Status Updated!!!";
    }

    @GetMapping("/recentAdded/doctor/{doctorId}")
    public List<PatientListDto> recentlyAddedPatient(@PathVariable("doctorId") Long doctorId) {
        return patientService.recentlyAddedPatient(doctorId);
    }


    //Dashboard Chart


    @GetMapping("/{doctorId}/totalPatient")
    public int totalPatient(@PathVariable("doctorId") Long doctorId) {
        return patientService.totalNoOfPatient(doctorId);
    }

    @GetMapping("/{doctorId}/totalActivePatient")
    public int totalNoOfPatientAddedThisWeek(@PathVariable("doctorId") Long doctorId) {
        return patientService.totalNoOfPatientAddedThisWeek(doctorId);
    }


    @GetMapping("/{doctorId}/category")
    public List<String> patientCategory(@PathVariable("doctorId") Long doctorId) {
        return patientService.patientCategory(doctorId);
    }

    @GetMapping("/{doctorId}/gender")
    public List<String> gender(@PathVariable("doctorId") Long doctorId) {
        return patientService.gender(doctorId);
    }

    @GetMapping("/{doctorId}/activePatient")
    public List<String> weeklyPatientCountChart(@PathVariable("doctorId") Long doctorId) {
        return patientService.weeklyPatientCountChart(doctorId);
    }

    @GetMapping("/{doctorId}/bloodGroup")
    public List<String> bloodGroup(@PathVariable("doctorId") Long doctorId) {
        return patientService.bloodGroup(doctorId);
    }

    @GetMapping("/{doctorId}/ageChart")
    public List<String> ageChart(@PathVariable("doctorId") Long doctorId) {
        return patientService.ageChart(doctorId);
    }


    //Add-On feature Refer Patient

    @PutMapping("/{id}/doctor/{doctorId}")
    public String referPatients(@PathVariable("doctorId") Long doctorId, @PathVariable("id") Long patientId) {

        return patientService.referPatients(doctorId, patientId);
    }

    @GetMapping("/message/{doctorId}")
    public List<String> getMessageForReferredPatient(@PathVariable("doctorId") Long doctorId) {
        return patientService.getMessageForReferredPatient(doctorId);
    }

    @PutMapping("/changeMessage/{doctorId}")
    public String changeStatus(@PathVariable("doctorId") Long doctorId) {
        patientService.changeStatus(doctorId);
        return "All Messages have been deleted!!!";
    }

}
