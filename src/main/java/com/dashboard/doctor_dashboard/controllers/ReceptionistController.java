package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.AttributesDto;
import com.dashboard.doctor_dashboard.Utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.services.receptionist.ReceptionistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/receptionist")
public class ReceptionistController {

    @Autowired
    ReceptionistService receptionistService;

    @GetMapping("/doctorNames")
    public ResponseEntity<GenericMessage> doctorNames(){
        return receptionistService.getDoctorDetails();
    }

    @GetMapping("/appointmentList/{doctorId}")
    public ResponseEntity<GenericMessage> appointmentList(@PathVariable("doctorId") long doctorId,@RequestParam("pageNo") int pageNo){
        return receptionistService.getDoctorAppointments(doctorId,pageNo);
    }


    @PostMapping("/addVitals/{appointmentId}")
    public ResponseEntity<GenericMessage> addVitals(@PathVariable("appointmentId") Long appointmentId, @RequestBody AttributesDto vitalsDto){
        return receptionistService.addAppointmentVitals(vitalsDto,appointmentId);
    }

    @GetMapping("/getAllAppointments")
    public ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff(@RequestParam("pageNo") int pageNo){
        return receptionistService.todayAllAppointmentForClinicStaff(pageNo);
    }

}


