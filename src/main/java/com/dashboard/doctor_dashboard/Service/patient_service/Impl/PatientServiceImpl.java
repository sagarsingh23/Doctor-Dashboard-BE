package com.dashboard.doctor_dashboard.Service.patient_service.Impl;

import com.dashboard.doctor_dashboard.Entity.Patient;
import com.dashboard.doctor_dashboard.Entity.dtos.PatientDto;
import com.dashboard.doctor_dashboard.Entity.dtos.PatientListDto;
import com.dashboard.doctor_dashboard.Repository.PatientRepository;
import com.dashboard.doctor_dashboard.Service.patient_service.PatientService;
import com.dashboard.doctor_dashboard.exception.MyCustomException;
import com.dashboard.doctor_dashboard.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<PatientListDto> getAllPatientByDoctorId(Long doctorId) {
        List<Patient> patient =  patientRepository.getAllPatientByDoctorId(doctorId);
        List<PatientListDto> patientListDto = patient.stream()
                .map(value -> mapToDto2(value)).collect(Collectors.toList());

        return patientListDto;
    }

    @Override
    public PatientDto getPatientById(Long id) throws MyCustomException {
      try{
        Patient patient = patientRepository.findById(id).get();
          return mapToDto(patient);
       }catch ( Exception e) {
          throw new MyCustomException("Patient", "id", id);
      }

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
        value.setDoctorDetails(patient.getDoctorDetails());
        value.setAttributes(patient.getAttributes());
        value.setStatus(patient.getStatus());

        return patientRepository.save(value);
    }

    @Override
    public void deletePatientById(Long id) {
        patientRepository.deleteById(id);
    }


    @Override
    public List<PatientListDto> recentlyAddedPatient(Long doctorId) {
        List<Patient> patient =  patientRepository.recentlyAddedPatient(doctorId);
        List<PatientListDto> patientListDto = patient.stream()
                .map(value -> mapToDto2(value)).collect(Collectors.toList());

        return patientListDto;
    }




    //DashBoard Chart

    @Override
    public int totalNoOfPatient(Long doctorId) {
        return patientRepository.totalNoOfPatient(doctorId);
    }

    @Override
    public void changePatientStatus(Long id, String status) {
        patientRepository.changePatientStatus(id, status);
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
        return  patientRepository.bloodGroup(doctorId);
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

        patientRepository.referPatients(doctorId,patientId,docName,patientName);
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
    private PatientDto mapToDto(Patient patient){
        PatientDto patientDto = mapper.map(patient,PatientDto.class);
        return patientDto;
    }

    private PatientListDto mapToDto2(Patient patient){
        PatientListDto patientListDto = mapper.map(patient,PatientListDto.class);
        return patientListDto;
    }



    //convert dto to entity

    private Patient mapToEntity(PatientDto patientDto){
        Patient patient = mapper.map(patientDto,Patient.class);
        return patient;
    }


}
