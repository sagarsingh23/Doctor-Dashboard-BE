package com.dashboard.doctor_dashboard.Controller;

import com.dashboard.doctor_dashboard.Entity.Attributes;
import com.dashboard.doctor_dashboard.Entity.dtos.NotesDto;
import com.dashboard.doctor_dashboard.Service.patient_service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/attribute")
@CrossOrigin(origins = "http://localhost:3000")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    @PostMapping()
    public Attributes addAttribute(@RequestBody Attributes attributes) {
        return attributeService.addAttribute(attributes);
    }

    @GetMapping()
    public List<Attributes> getAllAttribute() {
        return attributeService.getAllAttribute();
    }


    @GetMapping("/{id}")
    public Attributes getAttributeById(@PathVariable("id") Long id) {
        return attributeService.getAttributeById(id);
    }


    @PutMapping("/{id}")
    public Attributes updateAttribute(@PathVariable("id") Long id, @RequestBody Attributes attributes) {
        return attributeService.updateAttribute(id, attributes);
    }

    @DeleteMapping("/{id}")
    public String deleteAttributeById(@PathVariable("id") Long id) {
        attributeService.deleteAttributeById(id);
        return "Successfully Deleted";

    }

    @PutMapping("/changeNotes/{id}")
    public String changePatientStatus(@PathVariable("id") Long id,@RequestBody NotesDto notes){
        attributeService.changeNotes(id,notes.getNotes());
        return "Notes updated!!!";
    }

}
