package com.dashboard.doctor_dashboard.services.doctor_service;


import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;

import com.dashboard.doctor_dashboard.entities.dtos.*;

import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;


    @Autowired
    private LoginRepo loginRepo;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

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
//<<<<<<< HEAD
//    public DoctorBasicDetailsDto getDoctorById(long id) {
//        if (doctorRepository.isIdAvailable(id) != null && loginRepo.isIdAvailable(id)!=null)
//            return doctorRepository.findDoctorById(id);
//=======
    public ResponseEntity<GenericMessage> getDoctorById(long id) {

        if (doctorRepository.isIdAvailable(id) != null) {
            genericMessage.setData(doctorRepository.findDoctorById(id));
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }

//>>>>>>> 71f06e33a9ec991c695a56bd29b24f86ef4c2418
        return null;
    }

    @Override
//<<<<<<< HEAD
    public ResponseEntity<GenericMessage> addDoctorDetails(DoctorFormDto details, long id, HttpServletRequest request) {

        Long doctorLoginId=jwtTokenProvider.getIdFromToken(request);
        System.out.println(doctorLoginId);
        if (loginRepo.isIdAvailable(doctorLoginId) != null) {

            if(doctorRepository.isIdAvailable(details.getId())==null){
                if (details.getId() == id && details.getId()==doctorLoginId) {
                    doctorRepository.insertARowIntoTheTable(details.getId(),details.getAge(),details.getSpeciality(),details.getPhoneNo(),details.getGender(),doctorLoginId);
                    genericMessage.setData( doctorRepository.getDoctorById(details.getId()));
                    genericMessage.setStatus(Constants.SUCCESS);
                    return new ResponseEntity<>(genericMessage,HttpStatus.OK);
                }
            }
            else if(doctorRepository.isIdAvailable(details.getId())!=null)
                throw new APIException(HttpStatus.BAD_REQUEST,"update not allowed in this API endpoint.");
        }

        throw new ResourceNotFoundException("doctor", "id", id);

    }

    @Override
    public ResponseEntity<GenericMessage>  updateDoctor(DoctorFormDto details, long id, HttpServletRequest request) {

        Long doctorLoginId = jwtTokenProvider.getIdFromToken(request);
        System.out.println(loginRepo.isIdAvailable(doctorLoginId) + ", " + doctorLoginId);
        if (loginRepo.isIdAvailable(doctorLoginId) != null) {

            if (doctorRepository.isIdAvailable(details.getId()) != null) {
//                if (details.getId() == id && details.getId()==doctorLoginId) {
//                    doctorRepository.insertARowIntoTheTable(details.getId(),details.getAge(),details.getSpeciality(),details.getPhoneNo(),details.getGender(),doctorLoginId);
//                    return doctorRepository.getDoctorById(details.getId());
//                }
                if (details.getId() == id && details.getId() == doctorLoginId) {
                    doctorRepository.updateDoctorDb(details.getAge(), details.getSpeciality(), details.getGender(), details.getPhoneNo(), details.getId());
                    genericMessage.setData( doctorRepository.getDoctorById(details.getId()));
                    genericMessage.setStatus(Constants.SUCCESS);
                    return new ResponseEntity<>(genericMessage,HttpStatus.OK);
//                    return doctorRepository.getDoctorById(details.getId());
                }

                return null;

            }

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
