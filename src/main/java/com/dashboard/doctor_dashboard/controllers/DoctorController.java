package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@SuppressWarnings("squid:S1612")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/doctor")
public class DoctorController {


    private DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * @param id is used as path variable
     * @return get all doctor
     */
    @ApiOperation("get all doctor")
    @GetMapping("/get-all/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> getAllDoctors(@PathVariable("doctorId") Long id) {

        ResponseEntity<GenericMessage> details = doctorService.getAllDoctors(id);
        if (details != null)
            return details;
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * @param id is used as path variable
     * @return doctor details on the basis of id provided
     */
    @ApiOperation("return doctor details on the basis of id provided")
    @GetMapping("/id/{id}")
    public ResponseEntity<GenericMessage> getDoctorById(@PathVariable("id") long id) {
        if (doctorService.getDoctorById(id) != null)
            return doctorService.getDoctorById(id);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * @param id is used as path variable
     * @param details consist of fields id,age,gender,phoneNo,speciality,exp and degree
     * @param bindingResult BindingResult
     * @param request HTTPServletRequest
     * @return a message "details added successfully" after Saving doctor details in the database
     */
    @ApiOperation("return a message-details added successfully, after Saving doctor details in the database")
    @PostMapping("/add-doctor-details/{id}")
    public ResponseEntity<GenericMessage>  addDoctorDetails(@Valid @RequestBody DoctorFormDto details, @PathVariable("id") long id, BindingResult bindingResult, HttpServletRequest request){
        var doctorFormDto = doctorService.addDoctorDetails(details,id,request);
        if (doctorFormDto != null)
            return doctorFormDto;
        throw new APIException("Id Mismatch");
    }

    /**
     * @param id is used as path variable
     * @param details consist of fields phone No and doctor id
    // * @param bindingResult bindingResult
     * @param request HTTPServletRequest
     * @return a string updated successfully after updating all the fields in database
     */

    @ApiOperation("return a string updated successfully after updating all the fields in database")
    @PutMapping("/update/{id}")
    public ResponseEntity<GenericMessage>  updateDoctorDetails(@Valid @RequestBody DoctorFormDto details,@PathVariable("id") long id,HttpServletRequest request)  {
        var doctorFormDto = doctorService.updateDoctor(details,id,request);
        if (doctorFormDto != null)
            return doctorFormDto;
        throw new APIException("Id Mismatch");
    }

    /**
     * @param id is used as path variable
     * @return deleted successfully after triggering this api. Private API
     */
    @ApiOperation("return deleted successfully after triggering this api. Private API")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deleteDoctor(@PathVariable("id") int id) {
        return doctorService.deleteDoctor(id);
    }

    /**
     * @param speciality is used as path variable
     * @return all the doctor present in the database according the speciality provided.
     */
    @ApiOperation("return all the doctor present in the database according the speciality provided")
    @GetMapping("/get-all-doctor/{speciality}")
    public ResponseEntity<GenericMessage> getAllDoctorsBySpeciality(@PathVariable("speciality") String speciality) {
        return doctorService.getAllDoctorsBySpeciality(speciality);
    }

    /**
     * @param doctorId is used as path variable
     * @return genders of the patients for doctor,and it's count for gender chart
     */
    @ApiOperation("return genders of the patients for doctor,and it's count for gender chart")
    @GetMapping("/{doctorId}/gender")
    public ResponseEntity<GenericMessage> genderChart(@PathVariable("doctorId") Long doctorId) {
        return doctorService.genderChart(doctorId);
    }

    /**
     * @param doctorId is used as path variable
     * @return Blood groups of the patients for doctor,and it's count for blood group chart
     */
    @ApiOperation("return Blood groups of the patients for doctor,and it's count for blood group chart")
    @GetMapping("/{doctorId}/bloodGroup")
    public ResponseEntity<GenericMessage> bloodGroupChart(@PathVariable("doctorId") Long doctorId) {
        return doctorService.bloodGroupChart(doctorId);
    }

    /**
     * @param doctorId is used as path variable
     * @return age groups of the patients for doctor,and it's count for age group chart
     */
    @ApiOperation("return age groups of the patients for doctor,and it's count for age group chart")
    @GetMapping("/{doctorId}/ageGroup")
    public ResponseEntity<GenericMessage> ageGroup(@PathVariable("doctorId") Long doctorId) {
        return doctorService.ageGroupChart(doctorId);
    }
}
