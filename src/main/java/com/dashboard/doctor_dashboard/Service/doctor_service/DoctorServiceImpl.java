package com.dashboard.doctor_dashboard.Service.doctor_service;


import com.dashboard.doctor_dashboard.Entity.doctor_entity.DoctorDetails;
import com.dashboard.doctor_dashboard.Entity.dto.DoctorSpecialityDto;
import com.dashboard.doctor_dashboard.Repository.doctor_repository.DoctorRepository;
import com.dashboard.doctor_dashboard.jwt.exception.APIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public List<DoctorDetails>  getDoctorByFirstName(String name) {
        return repository.findByFirstName(name);
    }
    @Override
    public List<DoctorDetails>  getDoctorByLastName(String name) {
        return repository.findByLastName(name);
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
    public DoctorSpecialityDto getDoctorBySpeciality(long id) {

        DoctorSpecialityDto doctorSpecialityDto =new DoctorSpecialityDto();
        String speciality=repository.findBySpeciality(id);
        System.out.println(speciality);
                doctorSpecialityDto.setSpeciality(speciality);
        return doctorSpecialityDto;
    }

    @Override
    public DoctorDetails updateDoctor(DoctorDetails details, long id) {
        if(details.getId()==id){
            String email= repository.findEmail(id);
            System.out.println(email+" "+details.getEmail());
            System.out.println(email.equals( details.getEmail()));
            if(!email.equals( details.getEmail())) {
                throw new APIException(HttpStatus.FORBIDDEN, "change in email not allowed");
            }
            return repository.save(details);
        }
        return null;
    }

    @Override
    public String deleteDoctor(long id) {
        repository.deleteById(id);
        return "Successfully deleted";
    }
}
