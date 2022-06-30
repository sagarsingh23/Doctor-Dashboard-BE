package com.dashboard.doctor_dashboard.services.patient_service;

import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientDetailsUpdateDto;
import com.dashboard.doctor_dashboard.entities.dtos.PatientEntityDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PatientService {
    ResponseEntity<GenericMessage> addPatient(PatientEntityDto patient, Long loginId);

    ResponseEntity<GenericMessage> getPatientDetailsById(Long loginId);

    ResponseEntity<GenericMessage> deletePatientById(Long id);

    ResponseEntity<GenericMessage> updatePatientDetails(Long id, PatientDetailsUpdateDto patientDetailsUpdateDto);

    ResponseEntity<GenericMessage> viewAppointment(Long appointmentId, long patientId);

    ResponseEntity<GenericMessage> getNotifications(long patientId);

}