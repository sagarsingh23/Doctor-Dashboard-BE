package com.dashboard.doctor_dashboard.services.prescription_service;

import com.dashboard.doctor_dashboard.entities.Prescription;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.UpdatePrescriptionDto;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@Service
public interface PrescriptionService {

    String addPrescription(Long id, UpdatePrescriptionDto updatePrescriptionDto) throws IOException, MessagingException, JSONException;

    List<Prescription> getAllPrescriptionByAppointment(Long appointId);

    ResponseEntity<GenericMessage> deleteAppointmentById(Long id);
}
