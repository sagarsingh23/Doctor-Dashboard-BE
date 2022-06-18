package com.dashboard.doctor_dashboard.services.prescription_service;

import com.dashboard.doctor_dashboard.entities.Prescription;
import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
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

    @Autowired
    private AttributeRepository attributeRepository;




    @Override
    public String addPrescription(Long appointId, UpdatePrescriptionDto updatePrescriptionDto) {
        if(appointmentRepository.getId(appointId) != null) {
            if (appointId == updatePrescriptionDto.getPrescriptions().get(0).getAppointment().getAppointId()) {
                prescriptionRepository.saveAll(updatePrescriptionDto.getPrescriptions());
                attributeRepository.changeNotes(appointId, updatePrescriptionDto.getNotes());
                appointmentRepository.changeAppointmentStatus(appointId, updatePrescriptionDto.getStatus());
                return "Prescription Added";
            }
            throw new ResourceNotFoundException("Appointment", "id", updatePrescriptionDto.getPrescriptions().get(0).getAppointment().getAppointId());
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
