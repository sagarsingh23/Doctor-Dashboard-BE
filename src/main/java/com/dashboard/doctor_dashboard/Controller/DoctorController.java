package com.dashboard.doctor_dashboard.Controller;

import com.dashboard.doctor_dashboard.Entity.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.Entity.dtos.DoctorSpecialityDto;
import com.dashboard.doctor_dashboard.Entity.DoctorDetails;
import com.dashboard.doctor_dashboard.Service.doctor_service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService service;

    @GetMapping
    public List<DoctorDetails> getAllDoctors(){
        return service.getAllDoctors();
    }

    @GetMapping("/id/{id}")
    public DoctorDetails getDoctorsById(@PathVariable("id") long id){
        return service.getDoctorById(id);
    }


    @GetMapping("/name/{name}")
    public List<DoctorDetails> getDoctorsByName(@PathVariable("name") String name){
        return service.getDoctorByFirstName(name);
    }

    @GetMapping("/age/{age}")
    public List<DoctorDetails> getDoctorsByAge(@PathVariable("age") short age){
        return service.getDoctorByAge(age);
    }


    @GetMapping("/email/{email}")
    public DoctorDetails getDoctorsByEmail(@PathVariable("email") String email){
        return service.getDoctorByEmail(email);
    }
    @GetMapping("/speciality/{id}")
    public DoctorSpecialityDto getDoctorBySpeciality(@PathVariable("id") long id){
        return service.getDoctorBySpeciality(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DoctorFormDto> updateDoctorDetails(@PathVariable("id") long id, @Valid @RequestBody DoctorFormDto details){
        DoctorFormDto doctorFormDto=service.updateDoctor(details,id);
        if(doctorFormDto!=null)
            return new ResponseEntity(doctorFormDto, HttpStatus.CREATED);
        return new ResponseEntity("id mismatch",HttpStatus.BAD_REQUEST);
    }




    @PostMapping("/add/")
    public DoctorDetails addNewDoctor(@RequestBody DoctorDetails doctorDetails){
        return service.addDoctor(doctorDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteDoctor(@PathVariable("id") int id){
        return service.deleteDoctor(id);
    }

}
