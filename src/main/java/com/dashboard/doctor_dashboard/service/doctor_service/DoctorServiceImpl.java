package com.dashboard.doctor_dashboard.service.doctor_service;


import com.dashboard.doctor_dashboard.entity.DoctorDetails;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.exception.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {
    //    public  DoctorServiceImpl(){
//
//    }
    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public DoctorDetails addDoctor(DoctorDetails details) {
        return doctorRepository.save(details);
    }

    @Override
    public List<DoctorListDto> getAllDoctors(Long id) {

        if (doctorRepository.isIdAvailable(id) != null)
            return doctorRepository.getAllDoctors(id);

        throw new ResourceNotFoundException("doctor", "id", id);

    }

    @Override
    public DoctorBasicDetailsDto getDoctorById(long id) {
        if (doctorRepository.isIdAvailable(id) != null)
            return doctorRepository.findDoctorById(id);
        return null;
    }

//    @Override
//    public List<DoctorDetails>  getDoctorByFirstName(String name) {
//        return doctorRepository.findByFirstName(name);
//    }
//    @Override
//    public List<DoctorDetails>  getDoctorByLastName(String name) {
//        return doctorRepository.findByLastName(name);
//    }
//
//    @Override
//    public List<DoctorDetails>  getDoctorByAge(short age) {
//        return doctorRepository.findByAge(age);
//    }
//
//    @Override
//    public DoctorDetails getDoctorByEmail(String email) {
//        return doctorRepository.findByEmail(email);
//    }

//    @Override
//    public DoctorSpecialityDto getDoctorBySpeciality(long id) {
//
//        DoctorSpecialityDto doctorSpecialityDto =new DoctorSpecialityDto();
//        String speciality=doctorRepository.findBySpeciality(id);
//        System.out.println(speciality);
//                doctorSpecialityDto.setSpeciality(speciality);
//        return doctorSpecialityDto;
//    }

    @Override
    public DoctorFormDto updateDoctor(DoctorFormDto details, long id) {
        if (doctorRepository.isIdAvailable(id) != null) {
            if (details.getId() == id) {
                doctorRepository.updateDoctorDb(details.getAge(), details.getSpeciality(), details.getGender(), details.getPhoneNo(), id);
                return doctorRepository.getDoctorById(id);
            }
            return null;
        }
        throw new ResourceNotFoundException("doctor", "id", id);
    }

    @Override
    public String deleteDoctor(long id) {
        doctorRepository.deleteById(id);
        return "Successfully deleted";
    }
}
