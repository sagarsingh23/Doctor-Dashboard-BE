package com.dashboard.doctor_dashboard.services.doctor_service;

import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DoctorService {
    DoctorDetails addDoctor(DoctorDetails details);

    ResponseEntity<GenericMessage> getAllDoctors(Long id);

    ResponseEntity<GenericMessage> getDoctorById(long id);

    ResponseEntity<GenericMessage> updateDoctor(DoctorFormDto details, long id);

    ResponseEntity<GenericMessage> deleteDoctor(long id);
}
