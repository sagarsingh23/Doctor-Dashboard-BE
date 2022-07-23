package com.dashboard.doctor_dashboard.services.doctor_service;


import com.dashboard.doctor_dashboard.entities.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
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

/**
 * DoctorServiceImpl
 */
@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private  DoctorRepository doctorRepository;
    private  LoginRepo loginRepo;
    private  JwtTokenProvider jwtTokenProvider;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, LoginRepo loginRepo, JwtTokenProvider jwtTokenProvider) {
        this.doctorRepository = doctorRepository;
        this.loginRepo = loginRepo;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * This function of service is for getting all doctors
     * @param id  this variable contains Id.
     * @return It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> getAllDoctors(Long id){
        log.info("inside: DoctorServiceImpl::getAllDoctors");


        var genericMessage = new GenericMessage();

        if (doctorRepository.isIdAvailable(id) != null) {
            List<DoctorListDto> list = doctorRepository.getAllDoctors(id);
            genericMessage.setData(list);
            genericMessage.setStatus(Constants.SUCCESS);
            log.info("exit: DoctorServiceImpl::getAllDoctors");

            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }


        log.info("DoctorServiceImpl::getAllDoctors"+Constants.PATIENT_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);

    }
    /**
     * This function of service is for getting doctor details
     * @param id  this variable contains Id.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> getDoctorById(long id){
        log.info("inside: DoctorServiceImpl::getDoctorById");


        var genericMessage = new GenericMessage();

        if (doctorRepository.isIdAvailable(id) != null) {
            genericMessage.setData(doctorRepository.findDoctorById(id));
            genericMessage.setStatus(Constants.SUCCESS);
            log.info("exit: DoctorServiceImpl::getDoctorById");

            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }

        log.info("DoctorServiceImpl::getDoctorById"+Constants.DOCTOR_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * This function of service is for adding doctor details
     * @param details this variable contains details.
     * @param id this variable contains Id.
     * @param request this variable contains request.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> addDoctorDetails(DoctorFormDto details, long id, HttpServletRequest request){
        log.info("inside: DoctorServiceImpl::addDoctorDetails");


        var genericMessage = new GenericMessage();
        Long doctorLoginId=jwtTokenProvider.getIdFromToken(request);
        if (loginRepo.isIdAvailable(doctorLoginId) != null) {

            if(doctorRepository.isIdAvailable(details.getId())==null) {
                if (details.getId() == id && details.getId().equals(doctorLoginId)) {
                    doctorRepository.insertARowIntoTheTable(details.getId(),details.getAge(),details.getSpeciality(),details.getPhoneNo(),details.getGender(),doctorLoginId,details.getExp(),details.getDegree());
                    genericMessage.setData( doctorRepository.getDoctorById(details.getId()));
                    genericMessage.setStatus(Constants.SUCCESS);
                    log.debug("Doctor: Doctor on boarding completed.");
                    log.info("exit: DoctorServiceImpl::addDoctorDetails");
                    return new ResponseEntity<>(genericMessage,HttpStatus.OK);
                }
                throw new ResourceNotFoundException(Constants.DETAILS_MISMATCH);
            }
            else {
                log.error("Doctor Service Impl: update not allowed in this API endpoint.");
                throw new APIException("update not allowed in this API endpoint.");
            }
        }

        log.info("DoctorServiceImpl::addDoctorDetails"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * This function of service is for updating doctor details
     * @param details this variable contains details.
     * @param id this variable contains Id.
     * @param request this variable contains request.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage>  updateDoctor(UserDetailsUpdateDto details, long id, HttpServletRequest request){
        log.info("inside: DoctorServiceImpl::updateDoctor");


        var genericMessage = new GenericMessage();

        Long doctorLoginId = jwtTokenProvider.getIdFromToken(request);
        if (loginRepo.isIdAvailable(doctorLoginId) != null && doctorRepository.isIdAvailable(details.getId()) != null) {
            if (details.getId().equals(id) && details.getId().equals(doctorLoginId)) {
                doctorRepository.updateDoctorDb(details.getMobileNo());
                genericMessage.setData( doctorRepository.getDoctorById(details.getId()));
                genericMessage.setStatus(Constants.SUCCESS);
                log.info("exit: DoctorServiceImpl::updateDoctor");

                return new ResponseEntity<>(genericMessage,HttpStatus.OK);
            }

            log.info("DoctorServiceImpl::updateDoctor"+Constants.DETAILS_MISMATCH);

            throw new ResourceNotFoundException(Constants.DETAILS_MISMATCH);
        }
        log.info("DoctorServiceImpl::updateDoctor"+Constants.DOCTOR_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * this function of service is for deleting doctor details
     * @param id this variable contains Id.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> deleteDoctor(long id){
        log.info("inside: DoctorServiceImpl::deleteDoctor");


        var genericMessage = new GenericMessage();

        doctorRepository.deleteById(id);
        genericMessage.setData("Successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        log.debug("Doctor: Doctor deleted.");
        log.info("exit: DoctorServiceImpl::deleteDoctor");

        return new ResponseEntity<>(genericMessage,HttpStatus.OK);
    }
    /**
     * This function of service is for getting all doctor by speciality
     * @param speciality this variable contains speciality.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> getAllDoctorsBySpeciality(String speciality){
        log.info("inside: DoctorServiceImpl::getAllDoctorsBySpeciality");

        var genericMessage = new GenericMessage();

        if (doctorRepository.isSpecialityAvailable(speciality) != null) {
            List<DoctorListDto> list = doctorRepository.getAllDoctorsBySpeciality(speciality);
            genericMessage.setData(list);
            genericMessage.setStatus(Constants.SUCCESS);
            log.info("exit: DoctorServiceImpl::getAllDoctorsBySpeciality");

            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }
        log.info("DoctorServiceImpl::getAllDoctorsBySpeciality"+Constants.DOCTOR_NOT_FOUND_SPECIALITY);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND_SPECIALITY);
    }

    /**
     * This function of service is for fetching details for gender chart
     * @param doctorId this variable contains doctor Id.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> genderChart(Long doctorId){
        log.info("inside: DoctorServiceImpl::genderChart");

        Map<String,Integer> chart = new HashMap<>();
        if(doctorRepository.isIdAvailable(doctorId) != null) {

            List<String> genderChartValue = doctorRepository.genderChart(doctorId);

            for (String s:genderChartValue) {
                chart.put(s, Collections.frequency(genderChartValue,s));
            }
            log.info("exit: DoctorServiceImpl::genderChart");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);
        }
        log.info("DoctorServiceImpl::genderChart"+Constants.DOCTOR_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * This function of service is for fetching details for blood group chart
     * @param doctorId this variable contains doctor Id.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> bloodGroupChart(Long doctorId){
        log.info("inside: DoctorServiceImpl::bloodGroupChart");

        Map<String,Integer> chart = new HashMap<>();
        if(doctorRepository.isIdAvailable(doctorId) != null) {

            List<String> bloodGroupValue = doctorRepository.bloodGroupChart(doctorId);

            for (String s:bloodGroupValue) {
                chart.put(s, Collections.frequency(bloodGroupValue,s));
            }
            log.info("exit: DoctorServiceImpl::bloodGroupChart");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);
        }
        log.info("DoctorServiceImpl::bloodGroupChart"+Constants.DOCTOR_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * This function of service is for fetching details for age group chart
     * @param doctorId  this variable contains doctor Id.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> ageGroupChart(Long doctorId) {
        log.info("inside: doctorService:: ageGroupChart");
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
            log.info("exit: doctorService:: ageGroupChart");
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);

        }
        log.info("inside: doctorService:: ageGroupChart"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

}
