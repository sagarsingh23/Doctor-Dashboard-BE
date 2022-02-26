package com.dashboard.doctor_dashboard.Controller.patient_controller;

import com.dashboard.doctor_dashboard.Entity.patient_entity.Attributes;
import com.dashboard.doctor_dashboard.Service.patient_service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    @PostMapping("api/attribute")
    public Attributes addAttribute(@RequestBody Attributes attributes) {
        return attributeService.addAttribute(attributes);
    }

    @GetMapping("api/attribute")
    public List<Attributes> getAllAttribute() {
        return attributeService.getAllAttribute();
    }


    @GetMapping("api/attribute/{id}")
    public Attributes getAttributeById(@PathVariable("id") Long id) {
        return attributeService.getAttributeById(id);
    }


    @PutMapping("api/attribute/{id}")
    public Attributes updateAttribute(@PathVariable("id") Long id, @RequestBody Attributes attributes) {
        return attributeService.updateAttribute(id, attributes);
    }

    @DeleteMapping("api/attribute/{id}")
    public String deleteAttributeById(@PathVariable("id") Long id) {
        attributeService.deleteAttributeById(id);
        return "Successfully Deleted";

    }
}
