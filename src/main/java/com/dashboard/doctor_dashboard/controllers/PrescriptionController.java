package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.Utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.services.prescription_service.PrescriptionService;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/prescription")
@CrossOrigin(origins = "http://localhost:3000")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/{appointId}")
    public ResponseEntity<GenericMessage> addPrescription(@PathVariable("appointId") Long appointId, @RequestBody UpdatePrescriptionDto updatePrescriptionDto) throws MessagingException, JSONException, IOException {
        return prescriptionService.addPrescription(appointId,updatePrescriptionDto);
    }

    @GetMapping("/{appointId}")
    public ResponseEntity<GenericMessage> getALlPrescription(@PathVariable("appointId") Long appointId) {
        return prescriptionService.getAllPrescriptionByAppointment(appointId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deleteAppointment(@PathVariable("id") Long id) {
        return prescriptionService.deleteAppointmentById(id);
    }

}
