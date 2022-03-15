package com.dashboard.doctor_dashboard.Service.patient_service.Impl;

import com.dashboard.doctor_dashboard.Entity.Patient;
import com.dashboard.doctor_dashboard.Repository.PatientRepository;
import com.dashboard.doctor_dashboard.Service.patient_service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;


    @Override
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }


    @Override
    public List<Patient> getAllPatient() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).get();
    }

    @Override
    public Patient updatePatient(Long id, Patient patient) {

        Patient value = patientRepository.findById(id).get();
        value.setFullName(patient.getFullName());
        value.setAge(patient.getAge());
        value.setCategory(patient.getCategory());
        value.setEmailId(patient.getEmailId());
        value.setGender(patient.getGender());
        value.setLastVisitedDate(patient.getLastVisitedDate());
        value.setMobileNo(patient.getMobileNo());



        return patientRepository.save(value);
    }

    @Override
    public void deletePatientById(Long id) {

        patientRepository.deleteById(id);
    }

}
