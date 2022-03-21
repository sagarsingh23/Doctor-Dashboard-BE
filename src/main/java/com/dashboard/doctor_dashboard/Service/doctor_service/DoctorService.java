package com.dashboard.doctor_dashboard.Service.doctor_service;

import com.dashboard.doctor_dashboard.Entity.DoctorDetails;

import com.dashboard.doctor_dashboard.Entity.dto.DoctorSpecialityDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface DoctorService {
    public DoctorDetails addDoctor(DoctorDetails details);
    public List<DoctorDetails> getAllDoctors();
    public DoctorDetails getDoctorById(long id);
    public List<DoctorDetails> getDoctorByFirstName(String name);
    public List<DoctorDetails> getDoctorByLastName(String name);
    public List<DoctorDetails>  getDoctorByAge(short age);
    public DoctorDetails  getDoctorByEmail(String email);
    public DoctorSpecialityDto getDoctorBySpeciality(long id);
    public DoctorDetails updateDoctor(DoctorDetails details,long id);
    public String deleteDoctor(long id);
}
