package com.dashboard.doctor_dashboard.services.patient_service;

import com.dashboard.doctor_dashboard.entities.Patient;
import com.dashboard.doctor_dashboard.entities.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entities.dtos.PatientListDto;
import com.dashboard.doctor_dashboard.exceptions.MyCustomException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface PatientService {
    Patient addPatient(Patient patient);

    List<PatientListDto> getAllPatientByDoctorId(Long doctorId);

    PatientDto getPatientById(Long id, Long doctorId) throws MyCustomException;

    Patient updatePatient(Long id, Patient patient);

    void deletePatientById(Long id);

    void changePatientStatus(Long id, String status);

    List<PatientListDto> recentlyAddedPatient(Long doctorId);


    //chart
    int totalNoOfPatient(Long doctorId);

    int totalNoOfPatientAddedThisWeek(Long doctorId);

    ArrayList<String> patientCategory(Long doctorId);

    ArrayList<String> gender(Long doctorId);

    ArrayList<String> weeklyPatientCountChart(Long doctorId);

    ArrayList<String> bloodGroup(Long doctorId);

    ArrayList<String> ageChart(Long doctorId);


    //Add-On feature Refer Patient

    String referPatients(Long doctorId, Long patientId);

    ArrayList<String> getMessageForReferredPatient(Long doctorId);

    void changeStatus(Long doctorId);

}