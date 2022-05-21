package com.dashboard.doctor_dashboard.services.patient_service;

import com.dashboard.doctor_dashboard.entities.Patient;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entities.dtos.PatientEntityDto;
import com.dashboard.doctor_dashboard.entities.dtos.PatientListDto;
import com.dashboard.doctor_dashboard.exceptions.MyCustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface PatientService {
    ResponseEntity<GenericMessage> addPatient(PatientEntityDto patient, Long loginId);

    ResponseEntity<GenericMessage> getAllPatientByDoctorId(Long doctorId);

    ResponseEntity<GenericMessage> getPatientById(Long id, Long doctorId) throws MyCustomException;

    ResponseEntity<GenericMessage> updatePatient(Long id, Patient patient);

    ResponseEntity<GenericMessage> deletePatientById(Long id);

    ResponseEntity<GenericMessage> changePatientStatus(Long id, String status);

    ResponseEntity<GenericMessage> recentlyAddedPatient(Long doctorId);


    //chart
    ResponseEntity<GenericMessage> totalNoOfPatient(Long doctorId);

    ResponseEntity<GenericMessage> totalNoOfPatientAddedThisWeek(Long doctorId);

    ResponseEntity<GenericMessage> patientCategory(Long doctorId);

    ResponseEntity<GenericMessage> gender(Long doctorId);

    ResponseEntity<GenericMessage> weeklyPatientCountChart(Long doctorId);

    ResponseEntity<GenericMessage> bloodGroup(Long doctorId);

    ResponseEntity<GenericMessage> ageChart(Long doctorId);


    //Add-On feature Refer Patient

    ResponseEntity<GenericMessage> referPatients(Long doctorId, Long patientId);

    ResponseEntity<GenericMessage> getMessageForReferredPatient(Long doctorId);

    ResponseEntity<GenericMessage> changeStatus(Long doctorId);

    ResponseEntity<GenericMessage> updatePatientDetails(Long id, PatientEntityDto patient);


}