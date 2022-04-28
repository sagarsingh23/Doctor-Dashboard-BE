package com.dashboard.doctor_dashboard.controller;

import com.dashboard.doctor_dashboard.entity.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.exception.APIException;
import com.dashboard.doctor_dashboard.exception.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.exception.ValidationsException;
import com.dashboard.doctor_dashboard.service.doctor_service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/get-all/doctor/{doctorId}")
    public List<DoctorListDto> getAllDoctors(@PathVariable("doctorId") Long id) {

        List<DoctorListDto> details = doctorService.getAllDoctors(id);
        if (details != null)
            return details;
        throw new ResourceNotFoundException("doctor", "id", id);
    }

    @GetMapping("/id/{id}")
    public DoctorBasicDetailsDto getDoctorById(@PathVariable("id") long id) {
        if (doctorService.getDoctorById(id) != null)
            return doctorService.getDoctorById(id);
        throw new ResourceNotFoundException("doctor", "id", id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DoctorFormDto> updateDoctorDetails(@PathVariable("id") long id, @Valid @RequestBody DoctorFormDto details, BindingResult bindingResult, WebRequest webRequest) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            throw new ValidationsException(errors);
        }
        DoctorFormDto doctorFormDto = doctorService.updateDoctor(details, id);
        if (doctorFormDto != null)
            return new ResponseEntity<>(doctorFormDto, HttpStatus.OK);
        throw new APIException(HttpStatus.BAD_REQUEST, "id mismatch");
    }

    @DeleteMapping("/{id}")
    public String deleteDoctor(@PathVariable("id") int id) {
        return doctorService.deleteDoctor(id);
    }

}
