package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.AppointmentDto;
import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.services.appointment_service.AppointmentService;
import io.swagger.annotations.ApiOperation;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;


@RestController
@RequestMapping("api/v1/appointment")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {

    private AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * @param appointment is used as path variable
     * @param request request HTTPServletRequest
     * @return appointment booked successfully message after creating appointment in database.
     * @throws MessagingException
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    @ApiOperation("Saves the details in appointment table")
    @PostMapping("/patient")
    public ResponseEntity<GenericMessage> addAppointment(@Valid @RequestBody AppointmentDto appointment, HttpServletRequest request) throws MessagingException, JSONException, UnsupportedEncodingException {
        return appointmentService.addAppointment(appointment,request);
    }

    /**
     * @param date is used as path variable
     * @param doctorId is used as path variable
     * @return all the available slots present for doctor on selected date.
     */
    @ApiOperation("Shows all the available slots of the doctor")
    @GetMapping("/getAvailableSlots/{doctorId}/{date}")
    public ResponseEntity<GenericMessage> showAvailableSlots(@PathVariable String  date,@PathVariable("doctorId") Long doctorId){
         return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,appointmentService.checkSlots(LocalDate.parse(date),doctorId)),HttpStatus.OK);
    }

    /**
     * @param patientId is used as path variable
     * @param pageNo is used as path variable
     * @return all appointments of the patient.
     */
    @ApiOperation("Shows all appointments of the patient")
    @GetMapping("/getAllAppointments/patient/{patientId}")
    public ResponseEntity<GenericMessage> getAllAppointmentByPatientId(@PathVariable("patientId") Long patientId,@RequestParam("pageNo") int pageNo) {
        return appointmentService.getAllAppointmentByPatientId(patientId,pageNo) ;
    }

    /**
     * @param doctorId is used as path variable
     * @param pageNo is used as path variable
     * @return all appointments of the doctor.
     */
    @ApiOperation("Shows all appointments of the doctor")
    @GetMapping("/getAllAppointments/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> getAllAppointmentByDoctorId(@PathVariable("doctorId") Long doctorId,@RequestParam("pageNo") int pageNo) {
        return appointmentService.getAllAppointmentByDoctorId(doctorId,pageNo) ;
    }


    /**
     * @param appointId is used as path variable
     * @return appointment details of patient for the doctor.
     */
    @ApiOperation("appointment details of patient for the doctor")
    @GetMapping("/{appointId}/patient")
    public ResponseEntity<GenericMessage> getAppointmentById(@PathVariable("appointId") Long appointId) {
        return appointmentService.getAppointmentById(appointId);
    }

    /**
     * @param doctorId is used as path variable
     * @return no of appointment booked for the doctor per week in a month.
     */
    @ApiOperation("return no of appointment booked for the doctor per week in a month")
    @GetMapping("chart/{doctorId}/weeklyGraphDoctor")
    public ResponseEntity<GenericMessage> weeklyDoctorCountChart(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.weeklyDoctorCountChart(doctorId);
    }

    /**
     * @param patientId is used as path variable
     * @return no of appointment booked by patient per week in a month.
     */
    @ApiOperation("return no of appointment booked by patient per week in a month")
    @GetMapping("chart/{patientId}/weeklyGraphPatient")
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(@PathVariable("patientId") Long patientId) {
        return appointmentService.weeklyPatientCountChart(patientId);
    }


    /**
     * @param doctorId is used as path variable
     * @return today's latest 3 recent appointment booked for the doctor.
     */
    @ApiOperation("return today's latest 3 recent appointment booked for the doctor")
    @GetMapping("/recentAdded/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> recentAppointment(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.recentAppointment(doctorId);
    }


    /**
     * @param doctorId is used as path variable
     * @return total no of appointments for the doctor.
     */
    @ApiOperation("return total no of appointments for the doctor")
    @GetMapping("/chart/{doctorId}/totalPatient")
    public ResponseEntity<GenericMessage> totalNoOfAppointment(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.totalNoOfAppointment(doctorId);
    }

    /**
     * @param doctorId is used as path variable
     * @return today's appointment count for the doctor.
     */
    @ApiOperation("return today's appointment count for the doctor")
    @GetMapping("/chart/{doctorId}/todayAppointments")
    public ResponseEntity<GenericMessage> todayAppointments(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.todayAppointments(doctorId);
    }

    /**
     * @param doctorId is used as path variable
     * @return total no of appointment booked in a week for the doctor.
     */
    @ApiOperation("returns total no of appointment booked in a week for the doctor")
    @GetMapping("/chart/{doctorId}/totalActivePatient")
    public ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.totalNoOfAppointmentAddedThisWeek(doctorId);
    }

    /**
     * @param loginId is used as path variable
     * @return categories of the appointment booked ,and it's count according to the id provided.
     */
    @ApiOperation("returns categories of the appointment booked and it's count according to the id provided ")
    @GetMapping("/{loginId}/category")
    public ResponseEntity<GenericMessage> patientCategoryGraph(@PathVariable("loginId") Long loginId) {
        return appointmentService.patientCategoryGraph(loginId);
    }

    /**
     * @param appointId is used as path variable
     * @return detail of doctor for which patient is booking appointment again..
     */
    @ApiOperation("returns detail of doctor for which patient is booking appointment again")
    @GetMapping("/{appointId}")
    public ResponseEntity<GenericMessage>getFollowDetails(@PathVariable("appointId") Long appointId){
        return appointmentService.getFollowDetails(appointId);
    }

}
