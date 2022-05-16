package com.dashboard.doctor_dashboard.services.doctor_service;


import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

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
