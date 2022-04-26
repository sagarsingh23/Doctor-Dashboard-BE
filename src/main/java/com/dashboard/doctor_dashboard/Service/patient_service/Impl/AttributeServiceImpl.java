package com.dashboard.doctor_dashboard.Service.patient_service.Impl;


import com.dashboard.doctor_dashboard.Repository.AttributeRepository;
import com.dashboard.doctor_dashboard.Repository.PatientRepository;
import com.dashboard.doctor_dashboard.Service.patient_service.AttributeService;
import com.dashboard.doctor_dashboard.exception.ResourceNotFoundException;
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
        if(patientRepository.getId(id) != null && patientRepository.getId(id).equals(id)) {
            attributeRepository.changeNotes(id, notes);
            return "Notes updated!!!";
        }
        throw new ResourceNotFoundException("Patient", "id", id);
    }
}
