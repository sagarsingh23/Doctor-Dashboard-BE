package com.dashboard.doctor_dashboard.services.prescription_service;

import com.dashboard.doctor_dashboard.entities.Prescription;
import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionServiceImpl implements PrescriptionService   {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;



    @Override
    public List<Prescription> addPrescription(Long appointId,List<Prescription> prescription) {
        if(appointmentRepository.getId(appointId) != null){
            return prescriptionRepository.saveAll(prescription);

        }
        throw new ResourceNotFoundException("Appointment","id",appointId);
    }

    @Override
    public List<Prescription> getAllPrescriptionByAppointment(Long appointId) {
        if(appointmentRepository.getId(appointId) != null){
            return prescriptionRepository.getAllPrescriptionByAppointment(appointId);
        }
        throw new ResourceNotFoundException("Appointment","id",appointId);

    }

    @Override
    public ResponseEntity<GenericMessage> deleteAppointmentById(Long id) {
        GenericMessage genericMessage = new GenericMessage();
        prescriptionRepository.deleteById(id);
        genericMessage.setData("successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage, HttpStatus.OK);
    }

}
