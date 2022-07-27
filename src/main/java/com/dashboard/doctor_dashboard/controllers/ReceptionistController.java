package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.AttributesDto;
import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.services.ReceptionistService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/receptionist")
@Slf4j
public class ReceptionistController {


    private final ReceptionistService receptionistService;

    @Autowired
    public ReceptionistController(ReceptionistService receptionistService) {
        this.receptionistService = receptionistService;
    }

    /**
     * @return This API is responsible for returning all the doctor names present in the database
     */
    @ApiOperation("return list of the doctor names present in the database")
    @GetMapping("/doctor-names")
    public ResponseEntity<GenericMessage> doctorNames(){
        log.info("ReceptionistController::doctorNames");
        return receptionistService.getDoctorDetails();
    }

    /**
     * @param doctorId is used as path variable
     * @param pageNo is used as path variable
     * @return list of the today's appointments for the doctor
     */
    @ApiOperation("return list of the today's appointments for the doctor")
    @GetMapping("/appointment-list/{doctorId}")
    public ResponseEntity<GenericMessage> appointmentList(@PathVariable("doctorId") long doctorId,@RequestParam("pageNo") int pageNo,@RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize){
        log.info("ReceptionistController:: appointmentList");

        return receptionistService.getDoctorAppointments(doctorId,pageNo,pageSize);
    }


    /**
     * @param appointmentId is used as path variable
     * @param vitalsDto is used as path variable
     * @return Vitals updated message after updating vitals in the database
     */
    @ApiOperation("API to update patient vitals")
    @PostMapping("/vitals/{appointmentId}")
    public ResponseEntity<GenericMessage> addVitals(@PathVariable("appointmentId") Long appointmentId, @Valid @RequestBody AttributesDto vitalsDto){
        log.info("ReceptionistController::addVitals");

        return receptionistService.addAppointmentVitals(vitalsDto,appointmentId);
    }

    /**
     * @param pageNo is used as path variable
     * @return All the today's to be attended appointment present in the database
     */
    @ApiOperation("return All the today's to be attended appointment present in the database")
    @GetMapping("/all-appointments")
    public ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff(@RequestParam("pageNo") int pageNo,@RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize){
        log.info("ReceptionistController:: todayAllAppointmentForClinicStaff");

        return receptionistService.todayAllAppointmentForClinicStaff(pageNo,pageSize);
    }

}


