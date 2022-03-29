package com.dashboard.doctor_dashboard.Service.patient_service;

import com.dashboard.doctor_dashboard.Entity.Patient;
import com.dashboard.doctor_dashboard.Entity.dtos.PatientDto;
import com.dashboard.doctor_dashboard.Entity.dtos.PatientListDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface PatientService {
    public Patient addPatient(Patient patient);
    public List<PatientListDto> getAllPatientByDoctorId(Long doctorId);
    public PatientDto getPatientById(Long id);
    public Patient updatePatient(Long id,Patient patient);
    public void deletePatientById(Long id);
    public List<Patient> getAllPatient();
    public void changePatientStatus(Long id,String status);
    public List<PatientListDto> recentlyAddedPatient(Long doctorId);


    //chart
    public  int totalNoOfPatient(Long doctorId);
    public ArrayList<String> patientCategory(Long doctorId);
    public ArrayList<String> gender(Long doctorId);
    public ArrayList<String> activePatient(Long doctorId);
    public ArrayList<String> bloodGroup(Long doctorId);
    public ArrayList<String> ageChart(Long doctorId);


    //Add-On feature Refer Patient

    public String referPatients(Long doctorId,Long patientId);
    public ArrayList<String> getMessageForReferredPatient(Long doctorId);
    public void changeStatus(Long doctorId);

}