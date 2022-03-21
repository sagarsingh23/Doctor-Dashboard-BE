package com.dashboard.doctor_dashboard.Service.patient_service;

import com.dashboard.doctor_dashboard.Entity.Patient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface PatientService {
    public Patient addPatient(Patient patient);
    public List<Patient> getAllPatientByDoctorId(Long doctorId);
    public Patient getPatientById(Long id);
    public Patient updatePatient(Long id,Patient patient);
    public void deletePatientById(Long id);
    public  int totalNoOfPatient(Long doctorId);
    public ArrayList<String> patientCategory(Long doctorId);
    public ArrayList<String> gender(Long doctorId);
    public ArrayList<String> activePatient(Long doctorId);
    public ArrayList<String> bloodGroup(Long doctorId);
    public List<Patient> getAllPatient();
    public ArrayList<String> ageChart(Long doctorId);

}