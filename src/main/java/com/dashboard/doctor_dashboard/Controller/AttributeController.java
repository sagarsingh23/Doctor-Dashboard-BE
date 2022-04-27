package com.dashboard.doctor_dashboard.Controller;

import com.dashboard.doctor_dashboard.Entity.dtos.NotesDto;
import com.dashboard.doctor_dashboard.Service.patient_service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/attribute")
@CrossOrigin(origins = "http://localhost:3000")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;


    @PutMapping("/changeNotes/{id}")
    public String changeNotes(@PathVariable("id") Long id, @RequestBody NotesDto notes) {
        attributeService.changeNotes(id, notes.getNotes());
        return "Notes updated!!!";
    }

}
