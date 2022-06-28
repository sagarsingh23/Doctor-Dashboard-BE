package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.VitalsDto;
import com.dashboard.doctor_dashboard.services.receptionist.ReceptionistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/receptionist")
public class RecepetionistController {

    @Autowired
    ReceptionistService receptionistService;
    @GetMapping("/doctorNames")
    public ResponseEntity<GenericMessage> doctorNames(){
        return receptionistService.getDoctorDetails();
    }

    @GetMapping("/appointmentList/{doctorId}")
    public ResponseEntity<GenericMessage> appointmentList(@PathVariable("doctorId") long doctorId){
        return receptionistService.getDoctorAppointments(doctorId);
    }

//    @PutMapping("/updateVitals/{appointmentId}")
//    public ResponseEntity<GenericMessage> updateVitals(@PathVariable("appointmentId") Long appointmentId, @RequestBody VitalsDto vitalsUpdateDto){
//      return   receptionistService.updateAppointmentVitals(vitalsUpdateDto,appointmentId);
//    }

    @PostMapping("/addVitals/{appointmentId}")
    public ResponseEntity<GenericMessage> addVitals(@PathVariable("appointmentId") Long appointmentId, @RequestBody Attributes vitalsDto){
        return receptionistService.addAppointmentVitals(vitalsDto,appointmentId);
    }

    @GetMapping("/getAllAppointments")
    public ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff(){
        return receptionistService.todayAllAppointmentForClinicStaff();
    }

}


