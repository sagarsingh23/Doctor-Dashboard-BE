package com.dashboard.doctor_dashboard.services.patient_service;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface AttributeService {
    ResponseEntity<GenericMessage> changeNotes(Long appointId, String prescription);

    ResponseEntity<GenericMessage> updateAttributeByAppointmentId(Long appointId, Attributes attribute);
}
