package com.dashboard.doctor_dashboard.services.doctor_service;


import com.dashboard.doctor_dashboard.Utils.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.Utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;


    @Autowired
    private LoginRepo loginRepo;


    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @Override
    public ResponseEntity<GenericMessage> getAllDoctors(Long id) {

        var genericMessage = new GenericMessage();

        if (doctorRepository.isIdAvailable(id) != null) {
            List<DoctorListDto> list = doctorRepository.getAllDoctors(id);
            genericMessage.setData(list);
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);

    }

    @Override
     public ResponseEntity<GenericMessage> getDoctorById(long id) {

        var genericMessage = new GenericMessage();


        if (doctorRepository.isIdAvailable(id) != null) {
            genericMessage.setData(doctorRepository.findDoctorById(id));
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }
        throw new ResourceNotFoundException(Constants.LOGIN_DETAILS_NOT_FOUND);
    }

    @Override
    public ResponseEntity<GenericMessage> addDoctorDetails(DoctorFormDto details, long id, HttpServletRequest request) {

        var genericMessage = new GenericMessage();

        Long doctorLoginId=jwtTokenProvider.getIdFromToken(request);
        if (loginRepo.isIdAvailable(doctorLoginId) != null) {
            if(doctorRepository.isIdAvailable(details.getId())==null){
                if (details.getId() == id && details.getId().equals(doctorLoginId)) {
                    doctorRepository.insertARowIntoTheTable(details.getId(),details.getAge(),details.getSpeciality(),details.getPhoneNo(),details.getGender(),doctorLoginId,details.getExp(),details.getDegree());
                    log.debug("Doctor Service:: Doctor Added..");
                    genericMessage.setData( doctorRepository.getDoctorById(details.getId()));
                    genericMessage.setStatus(Constants.SUCCESS);
                    return new ResponseEntity<>(genericMessage,HttpStatus.CREATED);
                }
                throw new ResourceNotFoundException(Constants.DETAILS_MISMATCH);
            }
            else
                throw new APIException("update not allowed in this API endpoint.");
        }

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);

    }

    @Override
    public ResponseEntity<GenericMessage>  updateDoctor(DoctorFormDto details, long id, HttpServletRequest request) {

        var genericMessage = new GenericMessage();

        Long doctorLoginId = jwtTokenProvider.getIdFromToken(request);
        if (loginRepo.isIdAvailable(doctorLoginId) != null && doctorRepository.isIdAvailable(details.getId()) != null) {
                if (details.getId().equals(id) && details.getId().equals(doctorLoginId)) {
                    doctorRepository.updateDoctorDb(details.getPhoneNo());
                    genericMessage.setData( doctorRepository.getDoctorById(details.getId()));
                    genericMessage.setStatus(Constants.SUCCESS);
                    return new ResponseEntity<>(genericMessage,HttpStatus.OK);
                 }

                throw new ResourceNotFoundException(Constants.DETAILS_MISMATCH);
        }
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }


    @Override
    public ResponseEntity<GenericMessage> deleteDoctor(long id) {

        var genericMessage = new GenericMessage();

        doctorRepository.deleteById(id);
        genericMessage.setData("Successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> getAllDoctorsBySpeciality(String speciality) {
        var genericMessage = new GenericMessage();

        if (doctorRepository.isSpecialityAvailable(speciality) != null) {
            List<DoctorListDto> list = doctorRepository.getAllDoctorsBySpeciality(speciality);
            genericMessage.setData(list);
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND_SPECIALITY);
    }

    @Override
    public ResponseEntity<GenericMessage> genderChart(Long doctorId) {
        Map<String,Integer> chart = new HashMap<>();
        if(doctorRepository.isIdAvailable(doctorId) != null){

            List<String> genderChartValue = doctorRepository.genderChart(doctorId);

            for (String s:genderChartValue) {
                chart.put(s, Collections.frequency(genderChartValue,s));
            }
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);
        }
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    @Override
    public ResponseEntity<GenericMessage> bloodGroupChart(Long doctorId) {
         Map<String,Integer> chart = new HashMap<>();
        if(doctorRepository.isIdAvailable(doctorId) != null){

            List<String> bloodGroupValue = doctorRepository.bloodGroupChart(doctorId);

            for (String s:bloodGroupValue) {
                chart.put(s, Collections.frequency(bloodGroupValue,s));
            }
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);
        }
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    @Override
    public ResponseEntity<GenericMessage> ageGroupChart(Long doctorId) {
        Map<String,Integer> chart = new HashMap<>();
        var week1 = "0-2";
        var week2 = "3-14" ;
        var week3 = "15-25";
        var week4 = "26-64";
        var week5 = "65+";

        chart.put(week1,0);
        chart.put(week2,0);
        chart.put(week3,0);
        chart.put(week4,0);
        chart.put(week5,0);


        if(doctorRepository.isIdAvailable(doctorId) != null){
            List<Long> ageGroupValue = doctorRepository.ageGroupChart(doctorId);
            for (Long s:ageGroupValue) {

                if(s <= 2)
                {
                    chart.put(week1, chart.get(week1)+1);
                } else if (s<=14) {
                    chart.put(week2,chart.get(week2)+1);
                } else if (s<=25) {
                    chart.put(week3,chart.get(week3)+1);
                } else if (s<=64) {
                    chart.put(week4,chart.get(week4)+1);
                } else {
                    chart.put(week5,chart.get(week5)+1);
                }

            }
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);

        }
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

}
