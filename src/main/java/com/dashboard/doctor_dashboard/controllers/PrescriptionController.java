package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.services.prescription_service.PrescriptionService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/prescription")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class PrescriptionController {


    private  PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    /**
     * @param appointId is used as path variable
     * @param updatePrescriptionDto contains field patientDto,prescription status,notes etc
     * @return a message prescription updated after saving prescription in the database
     * @throws MessagingException
     * @throws JSONException
     * @throws IOException
     */
    @ApiOperation("This API is used to save or post prescription in the database")
    @PostMapping("/{appointId}")
    public ResponseEntity<GenericMessage> addPrescription(@PathVariable("appointId") Long appointId,@Valid @RequestBody UpdatePrescriptionDto updatePrescriptionDto) throws MessagingException, JSONException, IOException {
        log.info("PrescriptionController::addPrescription");
        return prescriptionService.addPrescription(appointId,updatePrescriptionDto);
    }

    /**
     * @param appointId is used as path variable
     * @return List of prescription on the basis of appointment id provided
     */
    @ApiOperation("This API is used to get all prescription")
    @GetMapping("/{appointId}")
    public ResponseEntity<GenericMessage> getAllPrescription(@PathVariable("appointId") Long appointId) {
        log.info("PrescriptionController::getAllPrescription");

        return prescriptionService.getAllPrescriptionByAppointment(appointId);
    }

    /**
     * @param id is used as path variable
     * @return Appointment deleted after successful api call
     */
    @ApiOperation("This API is used for deleting Appointment from Appointment table")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deleteAppointment(@PathVariable("id") Long id) {
        log.info("PrescriptionController::deleteAppointment");
        return prescriptionService.deleteAppointmentById(id);
    }

}
