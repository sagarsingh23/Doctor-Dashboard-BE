package com.dashboard.doctor_dashboard.services.patient_service.impl;


import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import com.dashboard.doctor_dashboard.services.patient_service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeServiceImpl implements AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public String changeNotes(Long id, String notes) {
        if (patientRepository.getId(id) != null && patientRepository.getId(id).equals(id)) {
            attributeRepository.changeNotes(id, notes);
            return "Notes updated!!!";
        }
        throw new ResourceNotFoundException("Patient", "id", id);
    }
}
