package com.dashboard.doctor_dashboard.services.doctor_service;


import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    GenericMessage genericMessage = new GenericMessage();


    @Override
    public DoctorDetails addDoctor(DoctorDetails details) {
        return doctorRepository.save(details);
    }

    @Override
    public ResponseEntity<GenericMessage> getAllDoctors(Long id) {


        if (doctorRepository.isIdAvailable(id) != null) {
            List<DoctorListDto> list = doctorRepository.getAllDoctors(id);
            genericMessage.setData(list);
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }


        throw new ResourceNotFoundException("doctor", "id", id);

    }

    @Override
    public ResponseEntity<GenericMessage> getDoctorById(long id) {

        if (doctorRepository.isIdAvailable(id) != null) {
            genericMessage.setData(doctorRepository.findDoctorById(id));
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }

        return null;
    }

    @Override
    public ResponseEntity<GenericMessage> updateDoctor(DoctorFormDto details, long id) {
        if (doctorRepository.isIdAvailable(id) != null) {
            if (details.getId() == id) {
                doctorRepository.updateDoctorDb(details.getAge(), details.getSpeciality(), details.getGender(), details.getPhoneNo(), id);
                genericMessage.setData(doctorRepository.getDoctorById(id));
                genericMessage.setStatus(Constants.SUCCESS);
                return new ResponseEntity<>(genericMessage,HttpStatus.OK);
            }
            return null;
        }
        throw new ResourceNotFoundException("doctor", "id", id);
    }

    @Override
    public ResponseEntity<GenericMessage> deleteDoctor(long id) {
        doctorRepository.deleteById(id);
        genericMessage.setData("Successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage,HttpStatus.OK);
    }
}
