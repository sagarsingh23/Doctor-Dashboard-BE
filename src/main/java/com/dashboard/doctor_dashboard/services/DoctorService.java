package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface DoctorService {

    ResponseEntity<GenericMessage> getAllDoctors(Long id);

    ResponseEntity<GenericMessage> getDoctorById(long id);

    ResponseEntity<GenericMessage>  updateDoctor(UserDetailsUpdateDto details, long id, HttpServletRequest request);

    ResponseEntity<GenericMessage>  addDoctorDetails(DoctorFormDto details, long id, HttpServletRequest request);

    ResponseEntity<GenericMessage> deleteDoctor(long id);

    ResponseEntity<GenericMessage> getAllDoctorsBySpeciality(String speciality);

    ResponseEntity<GenericMessage> genderChart(Long doctorId);

    ResponseEntity<GenericMessage> bloodGroupChart(Long doctorId);

    ResponseEntity<GenericMessage> ageGroupChart(Long doctorId);

}
