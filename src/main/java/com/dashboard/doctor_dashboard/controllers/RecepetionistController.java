package com.dashboard.doctor_dashboard.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/receptionist")
public class RecepetionistController {

@GetMapping("/doctorNames")
    public void doctorNames(){

}


}


