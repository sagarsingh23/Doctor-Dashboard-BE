package com.dashboard.doctor_dashboard.service.doctor_service;

import com.dashboard.doctor_dashboard.entity.DoctorDetails;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorListDto;
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
