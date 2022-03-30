package com.dashboard.doctor_dashboard.Service.patient_service.Impl;


import com.dashboard.doctor_dashboard.Repository.AttributeRepository;
import com.dashboard.doctor_dashboard.Service.patient_service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeServiceImpl implements AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Override
    public void changeNotes(Long id, String notes) {
        attributeRepository.changeNotes(id, notes);
    }
}
