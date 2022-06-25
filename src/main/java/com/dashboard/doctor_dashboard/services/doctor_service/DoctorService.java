package com.dashboard.doctor_dashboard.services.doctor_service;

import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface DoctorService {
    DoctorDetails addDoctor(DoctorDetails details);

    ResponseEntity<GenericMessage> getAllDoctors(Long id);

    ResponseEntity<GenericMessage> getDoctorById(long id);

//<<<<<<< HEAD
    ResponseEntity<GenericMessage>  updateDoctor(DoctorFormDto details, long id, HttpServletRequest request);
    ResponseEntity<GenericMessage>  addDoctorDetails(DoctorFormDto details, long id, HttpServletRequest request);
//=======
//    ResponseEntity<GenericMessage> updateDoctor(DoctorFormDto details, long id);
//>>>>>>> 71f06e33a9ec991c695a56bd29b24f86ef4c2418

    ResponseEntity<GenericMessage> deleteDoctor(long id);

    ResponseEntity<GenericMessage> getAllDoctorsBySpeciality(String speciality);

    ResponseEntity<GenericMessage> genderChart(Long doctorId);

    ResponseEntity<GenericMessage> bloodGroupChart(Long doctorId);

    ResponseEntity<GenericMessage> ageGroupChart(Long doctorId);




}
