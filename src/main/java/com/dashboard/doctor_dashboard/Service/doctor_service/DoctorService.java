package com.dashboard.doctor_dashboard.Service.doctor_service;

import com.dashboard.doctor_dashboard.Entity.DoctorDetails;
import com.dashboard.doctor_dashboard.Entity.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.Entity.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.Entity.dtos.DoctorListDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DoctorService {
    DoctorDetails addDoctor(DoctorDetails details);

    List<DoctorListDto> getAllDoctors(Long id);

    DoctorBasicDetailsDto getDoctorById(long id);

    DoctorFormDto updateDoctor(DoctorFormDto details, long id);

    String deleteDoctor(long id);
}
