package com.dashboard.doctor_dashboard.Service.doctor_service;


import com.dashboard.doctor_dashboard.Entity.doctor_entity.DoctorDetails;
import com.dashboard.doctor_dashboard.Repository.doctot_repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {
    public  DoctorServiceImpl(){

    }
    @Autowired
    private DoctorRepository repository;

    @Override
    public DoctorDetails addDoctor(DoctorDetails details) {
       return repository.save(details);
    }

    @Override
    public List<DoctorDetails> getAllDoctors() {
       List<DoctorDetails> details= repository.findAll();
        return details;
    }

    @Override
    public DoctorDetails getDoctorById(long id) {
        return repository.findById(id).get();
    }

    @Override
    public List<DoctorDetails>  getDoctorByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<DoctorDetails>  getDoctorByAge(short age) {
        return repository.findByAge(age);
    }

    @Override
    public DoctorDetails getDoctorByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public List<DoctorDetails>  getDoctorBySpeciality(String speciality) {

        return repository.findBySpeciality(speciality);
    }

    @Override
    public DoctorDetails updateDoctor(DoctorDetails details, long id) {
        if(details.getId()==id)
        return repository.save(details);
        return null;
    }

    @Override
    public String deleteDoctor(long id) {
        repository.deleteById(id);
        return "Successfully deleted";
    }
}
