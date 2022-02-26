package com.dashboard.doctor_dashboard.Service.patient_service.Impl;


import com.dashboard.doctor_dashboard.Entity.patient_entity.Attributes;
import com.dashboard.doctor_dashboard.Repository.patient_repository.AttributeRepository;
import com.dashboard.doctor_dashboard.Service.patient_service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AttributeServiceImpl implements AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Override
    public Attributes addAttribute(Attributes attributes) {
        return attributeRepository.save(attributes);
    }

    @Override
    public List<Attributes> getAllAttribute() {
        return attributeRepository.findAll();
    }

    @Override
    public Attributes getAttributeById(Long id) {
        return attributeRepository.findById(id).get();
    }

    @Override
    public Attributes updateAttribute(Long id, Attributes attributes) {
        Attributes value = attributeRepository.findById(id).get();
        value.setBloodGroup(attributes.getBloodGroup());
        value.setBloodPressure(attributes.getBloodPressure());
        value.setBodyTemp(attributes.getBodyTemp());
        value.setGlucoseLevel(attributes.getGlucoseLevel());
        value.setCategory(attributes.getCategory());
        value.setNotes(attributes.getNotes());
        value.setLastVisitedDate(attributes.getLastVisitedDate());

        return attributeRepository.save(value);
    }

    @Override
    public void deleteAttributeById(Long id) {
        attributeRepository.deleteById(id);

    }
}
