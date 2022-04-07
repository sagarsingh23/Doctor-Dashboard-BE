package com.dashboard.doctor_dashboard.Service.doctor_service;

import com.dashboard.doctor_dashboard.Entity.DoctorDetails;

import com.dashboard.doctor_dashboard.Entity.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.Entity.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.Entity.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.Entity.dtos.DoctorSpecialityDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface DoctorService {
    public DoctorDetails addDoctor(DoctorDetails details);
    public List<DoctorListDto> getAllDoctors(Long id);
    public DoctorBasicDetailsDto getDoctorById(long id);
//    public List<DoctorDetails> getDoctorByFirstName(String name);
//    public List<DoctorDetails> getDoctorByLastName(String name);
//    public List<DoctorDetails>  getDoctorByAge(short age);
//    public DoctorDetails  getDoctorByEmail(String email);
//    public DoctorSpecialityDto getDoctorBySpeciality(long id);
    public DoctorFormDto updateDoctor(DoctorFormDto details, long id);
    public String deleteDoctor(long id);
}
