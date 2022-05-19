package com.dashboard.doctor_dashboard.services.patient_service.impl;


import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import com.dashboard.doctor_dashboard.services.patient_service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AttributeServiceImpl implements AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private PatientRepository patientRepository;

    GenericMessage genericMessage = new GenericMessage();


    @Override
    public ResponseEntity<GenericMessage> changeNotes(Long id, String notes) {
        if (patientRepository.getId(id) != null && patientRepository.getId(id).equals(id)) {
            attributeRepository.changeNotes(id, notes);
            genericMessage.setData("Notes updated!!!");
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage, HttpStatus.OK) ;
        }
        throw new ResourceNotFoundException("Patient", "id", id);
    }
}
