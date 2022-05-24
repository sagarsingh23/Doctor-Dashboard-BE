package com.dashboard.doctor_dashboard.services.patient_service.impl;


import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
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

    @Autowired
    private AppointmentRepository appointmentRepository;


    @Override
    public ResponseEntity<GenericMessage> changeNotes(Long appointId, String prescription) {
        GenericMessage genericMessage = new GenericMessage();

        if (appointmentRepository.getId(appointId) != null && appointmentRepository.getId(appointId).equals(appointId)) {
            attributeRepository.changeNotes(appointId,prescription);
            genericMessage.setData("Notes updated!!!");
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage, HttpStatus.OK) ;
        }
        throw new ResourceNotFoundException("Appointment","id",appointId);
    }

    @Override
    public ResponseEntity<GenericMessage> updateAttributeByAppointmentId(Long appointId, Attributes attribute) {
        GenericMessage genericMessage = new GenericMessage();

       if(appointmentRepository.getId(appointId) != null && appointmentRepository.getId(appointId).equals(appointId)){
           var value = attributeRepository.getAttribute(appointId);
           value.setBodyTemp(attribute.getBodyTemp());
           value.setBloodPressure(attribute.getBloodPressure());
           value.setGlucoseLevel(attribute.getGlucoseLevel());
           genericMessage.setData(attributeRepository.save(value));
           genericMessage.setStatus(Constants.SUCCESS);

           return new ResponseEntity<>(genericMessage, HttpStatus.OK) ;
       }
       throw new ResourceNotFoundException("Appointment","id",appointId);
    }
}
