package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientDetailsUpdateDto;
import com.dashboard.doctor_dashboard.entities.dtos.PatientEntityDto;
import com.dashboard.doctor_dashboard.services.patient_service.PatientService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/patient")
public class PatientController {


    private  PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    //CRUD operation for patient

    /**
     * @param patient consists field mobileNo,gender,age,bloodGroup,address and alternative mobile no
     * @param loginId is used as a path variable
     * @return patient added successfully after successful api call
     */
    @ApiOperation("This API is used to post or save patient details in database")
    @PostMapping("/{loginId}")
    public ResponseEntity<GenericMessage> addPatientDetails(@Valid @RequestBody PatientEntityDto patient,@PathVariable("loginId") Long loginId) {
        return patientService.addPatient(patient,loginId);
    }

    /**
     * @param patientId is used as a path variable
     * @param appointmentId is used as a path variable
     * @return appointment details from appointment database
     */
    @ApiOperation("This API is used to get appointment details from database")
    @GetMapping("/{patientId}/appointment/{appointmentId}")
    public ResponseEntity<GenericMessage> getAppointmentViewByAppointmentId(@PathVariable("patientId") long patientId, @PathVariable("appointmentId") long appointmentId){
        return patientService.viewAppointment(appointmentId,patientId);
    }

    /**
     * @param loginId is used as a path variable
     * @return patient details from patient database
     */
    @ApiOperation("This API is used to get patient details from database")
    @GetMapping("/patientProfile/{loginId}")
    public ResponseEntity<GenericMessage> patientProfile(@PathVariable("loginId") Long loginId){
        return patientService.getPatientDetailsById(loginId);
    }

    /**
     * @param id is used as a path variable
     * @param patientDetailsUpdateDto consist fields id and mobile number
     * @return patient updated successfully after successful api call
     */
    @ApiOperation("This API is used to update patient details in database")
    @PutMapping("/update/{id}")
    public ResponseEntity<GenericMessage> updatePatientDetails(@PathVariable("id") Long id,@Valid @RequestBody PatientDetailsUpdateDto patientDetailsUpdateDto) {
        return patientService.updatePatientDetails(id, patientDetailsUpdateDto);
    }

    /**
     * @param id is used as a path variable
     * @return Patient deleted after successful API call
     */
    @ApiOperation("This API is used for deleting patient from patients database")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deletePatientById(@PathVariable("id") Long id) {
        return patientService.deletePatientById(id);
    }

    /**
     * @param patientId is used as a path variable
     * @return list of notifications for the patient
     */
    @ApiOperation("This API is used to get all notification for patient")
    @GetMapping("/{patientId}/getNotifications")
    public ResponseEntity<GenericMessage> getNotifications(@PathVariable("patientId") Long patientId){
        return patientService.getNotifications(patientId);
    }

}
