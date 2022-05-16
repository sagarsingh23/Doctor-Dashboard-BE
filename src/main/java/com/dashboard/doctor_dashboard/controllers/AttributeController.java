package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.NotesDto;
import com.dashboard.doctor_dashboard.services.patient_service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/attribute")
@CrossOrigin(origins = "http://localhost:3000")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;


    @PutMapping("/changeNotes/{id}")
    public String updateNotes(@PathVariable("id") Long id, @RequestBody NotesDto notes) { //updateNotes
        attributeService.changeNotes(id, notes.getNotes());
        return "Notes updated!!!";
    }

}
