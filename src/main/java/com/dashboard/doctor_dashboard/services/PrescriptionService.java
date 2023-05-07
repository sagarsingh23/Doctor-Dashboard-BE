package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.dtos.UpdatePrescriptionDto;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
public interface PrescriptionService {

    ResponseEntity<GenericMessage> addPrescription(Long id, UpdatePrescriptionDto updatePrescriptionDto) throws IOException, MessagingException, JSONException;

    ResponseEntity<GenericMessage> getAllPrescriptionByAppointment(Long appointId);

    ResponseEntity<GenericMessage> deleteAppointmentById(Long id);
}
