package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PrescriptionDto;
import com.dashboard.doctor_dashboard.services.patient_service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/attribute")
@CrossOrigin(origins = "http://localhost:3000")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;


    @PutMapping("/changeNotes/{appointId}")
    public ResponseEntity<GenericMessage> updatePrescription(@PathVariable("appointId") Long appointId, @RequestBody PrescriptionDto notes) { //updateNotes
        return attributeService.changeNotes(appointId, notes.getPrescription());
    }

    @PutMapping("update/{appointId}")
    public ResponseEntity<GenericMessage> updateAttributes(@PathVariable("appointId") Long appointId, @RequestBody Attributes attributes){
        return attributeService.updateAttributeByAppointmentId(appointId,attributes);
    }

}
