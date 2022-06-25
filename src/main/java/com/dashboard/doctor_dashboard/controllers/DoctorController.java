package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.exceptions.ValidationsException;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("squid:S1612")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/get-all/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> getAllDoctors(@PathVariable("doctorId") Long id) {

        ResponseEntity<GenericMessage> details = doctorService.getAllDoctors(id);
        if (details != null)
            return details;
        throw new ResourceNotFoundException("doctor", "id", id);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<GenericMessage> getDoctorById(@PathVariable("id") long id) {
        if (doctorService.getDoctorById(id) != null)
            return doctorService.getDoctorById(id);
        throw new ResourceNotFoundException("doctor", "id", id);
    }

    @PostMapping("/add-doctor-details/{id}")
    public ResponseEntity<GenericMessage>  addDoctorDetails(@PathVariable("id") long id, @Valid @RequestBody DoctorFormDto details, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationsException(errors);
        }
        var doctorFormDto = doctorService.addDoctorDetails(details,id,request);
        if (doctorFormDto != null)
            return doctorFormDto;
        throw new APIException(HttpStatus.BAD_REQUEST, "id mismatch");
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<GenericMessage>  updateDoctorDetails(@PathVariable("id") long id, @Valid @RequestBody DoctorFormDto details, BindingResult bindingResult,HttpServletRequest request)  {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationsException(errors);
        }
        var doctorFormDto = doctorService.updateDoctor(details,id,request);
        if (doctorFormDto != null)
            return doctorFormDto;
        throw new APIException(HttpStatus.BAD_REQUEST, "id mismatch");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deleteDoctor(@PathVariable("id") int id) {
        return doctorService.deleteDoctor(id);
    }

    @GetMapping("/get-all-doctor/{speciality}")
    public ResponseEntity<GenericMessage> getAllDoctorsBySpeciality(@PathVariable("speciality") String speciality) {
        return doctorService.getAllDoctorsBySpeciality(speciality);
    }

    @GetMapping("/{doctorId}/gender")
    public ResponseEntity<GenericMessage> gender(@PathVariable("doctorId") Long doctorId) {
        return doctorService.genderChart(doctorId);
    }

    @GetMapping("/{doctorId}/bloodGroup")
    public ResponseEntity<GenericMessage> bloodGroup(@PathVariable("doctorId") Long doctorId) {
        return doctorService.bloodGroupChart(doctorId);
    }

    @GetMapping("/{doctorId}/ageGroup")
    public ResponseEntity<GenericMessage> ageGroup(@PathVariable("doctorId") Long doctorId) {
        return doctorService.ageGroupChart(doctorId);
    }
}
