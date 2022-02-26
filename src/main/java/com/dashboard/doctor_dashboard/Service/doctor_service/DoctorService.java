package com.dashboard.doctor_dashboard.Service.doctor_service;

import com.dashboard.doctor_dashboard.Entity.doctor_entity.DoctorDetails;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface DoctorService {
    public DoctorDetails addDoctor(DoctorDetails details);
    public List<DoctorDetails> getAllDoctors();
    public DoctorDetails getDoctorById(long id);
    public List<DoctorDetails> getDoctorByName(String name);
    public List<DoctorDetails>  getDoctorByAge(short age);
    public DoctorDetails  getDoctorByEmail(String email);
    public List<DoctorDetails>  getDoctorBySpeciality(String speciality);
    public DoctorDetails updateDoctor(DoctorDetails details,long id);
    public String deleteDoctor(long id);
}
