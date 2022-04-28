package com.dashboard.doctor_dashboard.service.patient_service.impl;

import com.dashboard.doctor_dashboard.entity.Attributes;
import com.dashboard.doctor_dashboard.entity.Patient;
import com.dashboard.doctor_dashboard.entity.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entity.dtos.PatientListDto;
import com.dashboard.doctor_dashboard.exception.MyCustomException;
import com.dashboard.doctor_dashboard.exception.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import com.dashboard.doctor_dashboard.service.patient_service.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    public static final String PATIENT = "Patient";


    @Autowired
    private ModelMapper mapper;


    @Override
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<PatientListDto> getAllPatientByDoctorId(Long doctorId) {
        List<Patient> patient = patientRepository.getAllPatientByDoctorId(doctorId);
        return patient.stream()
                .map(value -> mapToDto2(value)).collect(Collectors.toList());

    }

    @Override
    public PatientDto getPatientById(Long id, Long doctorId) throws MyCustomException {
        try {
            var patient = patientRepository.getPatientByIdAndDoctorId(id, doctorId);
            return mapToDto(patient);
        } catch (Exception e) {
            throw new MyCustomException(PATIENT, "id", id);
        }

    }

    @Override
    public Patient updatePatient(Long id, Patient patient) {

        Optional<Patient> patients = patientRepository.findById(id);
        Optional<Attributes> attributes = attributeRepository.findById(id);

        if (patients.isPresent() && attributes.isPresent()) {

            var value = patients.get();
            var value1 = attributes.get();


            value.setFullName(patient.getFullName());
            value.setAge(patient.getAge());
            value.setCategory(patient.getCategory());
            value.setEmailId(patient.getEmailId());
            value.setGender(patient.getGender());
            value.setLastVisitedDate(patient.getLastVisitedDate());
            value.setMobileNo(patient.getMobileNo());

            value1.setBloodGroup(patient.getAttributes().getBloodGroup());
            value1.setBloodPressure(patient.getAttributes().getBloodPressure());
            value1.setBodyTemp(patient.getAttributes().getBodyTemp());
            value1.setSymptoms(patient.getAttributes().getSymptoms());
            value1.setGlucoseLevel(patient.getAttributes().getGlucoseLevel());

            patientRepository.save(value);
            attributeRepository.save(value1);
            return value;
        } else {
            throw new ResourceNotFoundException(PATIENT, "id", id);
        }
    }

    @Override
    public void deletePatientById(Long id) {
        patientRepository.deleteById(id);
    }


    @Override
    public List<PatientListDto> recentlyAddedPatient(Long doctorId) {
        List<Patient> patient = patientRepository.recentlyAddedPatient(doctorId);
        return patient.stream()
                .map(value -> mapToDto2(value)).collect(Collectors.toList());

    }

    @Override
    public void changePatientStatus(Long id, String status) {
        if (patientRepository.getId(id) != null && patientRepository.getId(id).equals(id)) {
            patientRepository.changePatientStatus(id, status);
        } else {
            throw new ResourceNotFoundException(PATIENT, "id", id);
        }
    }


    //DashBoard Chart

    @Override
    public int totalNoOfPatient(Long doctorId) {
        return patientRepository.totalNoOfPatient(doctorId);
    }

    @Override
    public int totalNoOfActivePatient(Long doctorId) {
        return patientRepository.totalNoOfActivePatient(doctorId);
    }


    @Override
    public ArrayList<String> patientCategory(Long doctorId) {
        return patientRepository.patientCategory(doctorId);
    }

    @Override
    public ArrayList<String> gender(Long doctorId) {
        return patientRepository.gender(doctorId);
    }

    @Override
    public ArrayList<String> activePatient(Long doctorId) {
        return patientRepository.activePatient(doctorId);
    }

    @Override
    public ArrayList<String> bloodGroup(Long doctorId) {
        return patientRepository.bloodGroup(doctorId);
    }

    @Override
    public ArrayList<String> ageChart(Long doctorId) {
        return patientRepository.ageChart(doctorId);
    }


    //Add-On feature Refer Patient


    @Override
    public String referPatients(Long doctorId, Long patientId) {

        String docName = patientRepository.findDoctorNameByPatientId(patientId);
        String patientName = patientRepository.findPatientNameByPatientId(patientId);

        patientRepository.referPatients(doctorId, patientId, docName, patientName);
        return "SuccessFull";
    }

    @Override
    public ArrayList<String> getMessageForReferredPatient(Long doctorId) {
        return patientRepository.getMessageForReferredPatient(doctorId);
    }

    @Override
    public void changeStatus(Long doctorId) {
        patientRepository.changeStatus(doctorId);
    }


    // convert entity to dto
    private PatientDto mapToDto(Patient patient) {
        return mapper.map(patient, PatientDto.class);
    }

    private PatientListDto mapToDto2(Patient patient) {
        return mapper.map(patient, PatientListDto.class);

    }


}
