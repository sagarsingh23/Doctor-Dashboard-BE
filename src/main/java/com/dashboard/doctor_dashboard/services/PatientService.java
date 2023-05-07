package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.dtos.PatientEntityDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PatientService {
    ResponseEntity<GenericMessage> addPatient(PatientEntityDto patient, Long loginId);

    ResponseEntity<GenericMessage> getPatientDetailsById(Long loginId);

    ResponseEntity<GenericMessage> deletePatientById(Long id);

    ResponseEntity<GenericMessage> updatePatientDetails(Long id, UserDetailsUpdateDto userDetailsUpdateDto);

    ResponseEntity<GenericMessage> viewAppointment(Long appointmentId, long patientId);

    ResponseEntity<GenericMessage> getNotifications(long patientId);

}