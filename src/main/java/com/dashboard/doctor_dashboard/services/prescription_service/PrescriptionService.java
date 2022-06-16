package com.dashboard.doctor_dashboard.services.prescription_service;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.Prescription;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface PrescriptionService {

    List<Prescription> addPrescription(Long id,List<Prescription> prescription);

    List<Prescription> getAllPrescriptionByAppointment(Long appointId);

    ResponseEntity<GenericMessage> deleteAppointmentById(Long id);
}
