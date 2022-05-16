package com.dashboard.doctor_dashboard.services.patient_service;

import org.springframework.stereotype.Service;

@Service
public interface AttributeService {
    String changeNotes(Long id, String notes);
}
