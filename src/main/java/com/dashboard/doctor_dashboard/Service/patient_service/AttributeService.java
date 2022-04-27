package com.dashboard.doctor_dashboard.Service.patient_service;

import org.springframework.stereotype.Service;

@Service
public interface AttributeService {
    String changeNotes(Long id, String notes);
}
