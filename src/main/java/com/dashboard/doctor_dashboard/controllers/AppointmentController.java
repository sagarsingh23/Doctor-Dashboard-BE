package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.AppointmentDto;
import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.services.AppointmentService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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


/**
 * AppointmentController
 */

@RestController
@RequestMapping("api/v1/appointment")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

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
        log.info("AppointmentController::addAppointment");
        return appointmentService.addAppointment(appointment,request);
    }

    /**
     * @param date is used as path variable
     * @param doctorId is used as path variable
     * @return all the available slots present for doctor on selected date.
     */
    @ApiOperation("Shows all the available slots of the doctor")
    @GetMapping("/available-slots/{doctorId}/{date}")
    public ResponseEntity<GenericMessage> showAvailableSlots(@PathVariable String  date,@PathVariable("doctorId") Long doctorId){
        log.info("AppointmentController::showAvailableSlots");
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,appointmentService.checkSlots(LocalDate.parse(date),doctorId)),HttpStatus.OK);
    }

    /**
     * @param patientId is used as path variable
     * @param pageNo is used as path variable
     * @return all appointments of the patient.
     */
    @ApiOperation("Shows all appointments of the patient")
    @GetMapping("/all-appointments/patient/{patientId}")
    public ResponseEntity<GenericMessage> allAppointmentByPatientId(@PathVariable("patientId") Long patientId,@RequestParam("pageNo") int pageNo,@RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize) {
        log.info("AppointmentController:: allAppointmentByPatientId");
        return appointmentService.getAllAppointmentByPatientId(patientId,pageNo,pageSize) ;
    }

    /**
     * @param doctorId is used as path variable
     * @param pageNo is used as path variable
     * @return all appointments of the doctor.
     */
    @ApiOperation("Shows all appointments of the doctor")
    @GetMapping("/all-appointments/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> allAppointmentByDoctorId(@PathVariable("doctorId") Long doctorId,@RequestParam("pageNo") int pageNo,@RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize) {
        log.info("AppointmentController:: allAppointmentByDoctorId");
        return appointmentService.getAllAppointmentByDoctorId(doctorId, pageNo,pageSize );
    }


    /**
     * @param appointId is used as path variable
     * @return appointment details of patient for the doctor.
     */
    @ApiOperation("appointment details of patient for the doctor")
    @GetMapping("/{appointId}/patient")
    public ResponseEntity<GenericMessage> appointmentById(@PathVariable("appointId") Long appointId) {
        log.info("AppointmentController::getAppointmentById");

        return appointmentService.getAppointmentById(appointId);
    }

    /**
     * @param doctorId is used as path variable
     * @return no of appointment booked for the doctor per week in a month.
     */
    @ApiOperation("return no of appointment booked for the doctor per week in a month")
    @GetMapping("chart/{doctorId}/weekly-graph-doctor")
    public ResponseEntity<GenericMessage> weeklyDoctorCountChart(@PathVariable("doctorId") Long doctorId) {
        log.info("AppointmentController::weeklyDoctorCountChart");

        return appointmentService.weeklyDoctorCountChart(doctorId);
    }

    /**
     * @param patientId is used as path variable
     * @return no of appointment booked by patient per week in a month.
     */
    @ApiOperation("return no of appointment booked by patient per week in a month")
    @GetMapping("chart/{patientId}/weekly-graph-patient")
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(@PathVariable("patientId") Long patientId) {
        log.info("AppointmentController::weeklyPatientCountChart");

        return appointmentService.weeklyPatientCountChart(patientId);
    }


    /**
     * @param doctorId is used as path variable
     * @return today's latest 3 recent appointment booked for the doctor.
     */
    @ApiOperation("return today's latest 3 recent appointment booked for the doctor")
    @GetMapping("/recent-added/doctor/{doctorId}")
    public ResponseEntity<GenericMessage> recentAppointment(@PathVariable("doctorId") Long doctorId) {
        log.info("AppointmentController::recentAppointment");

        return appointmentService.recentAppointment(doctorId);
    }


    /**
     * @param doctorId is used as path variable
     * @return total no of appointments for the doctor.
     */
    @ApiOperation("return total no of appointments for the doctor")
    @GetMapping("/chart/{doctorId}/total-patient")
    public ResponseEntity<GenericMessage> totalNoOfAppointment(@PathVariable("doctorId") Long doctorId) {
        log.info("AppointmentController::totalNoOfAppointment");

        return appointmentService.totalNoOfAppointment(doctorId);
    }

    /**
     * @param doctorId is used as path variable
     * @return today's appointment count for the doctor.
     */
    @ApiOperation("return today's appointment count for the doctor")
    @GetMapping("/chart/{doctorId}/today-appointments")
    public ResponseEntity<GenericMessage> todayAppointments(@PathVariable("doctorId") Long doctorId) {
        log.info("AppointmentController::todayAppointments");

        return appointmentService.todayAppointments(doctorId);
    }

    /**
     * @param doctorId is used as path variable
     * @return total no of appointment booked in a week for the doctor.
     */
    @ApiOperation("returns total no of appointment booked in a week for the doctor")
    @GetMapping("/chart/{doctorId}/appointments-this-week")
    public ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(@PathVariable("doctorId") Long doctorId) {
        log.info("AppointmentController::totalNoOfAppointmentAddedThisWeek");

        return appointmentService.totalNoOfAppointmentAddedThisWeek(doctorId);
    }

    /**
     * @param loginId is used as path variable
     * @return categories of the appointment booked ,and it's count according to the id provided.
     */
    @ApiOperation("returns categories of the appointment booked and it's count according to the id provided ")
    @GetMapping("/{loginId}/category")
    public ResponseEntity<GenericMessage> patientCategoryGraph(@PathVariable("loginId") Long loginId) {
        log.info("AppointmentController::patientCategoryGraph");

        return appointmentService.patientCategoryGraph(loginId);
    }

    /**
     * @param appointId is used as path variable
     * @return detail of doctor for which patient is booking appointment again..
     */
    @ApiOperation("returns detail of doctor for which patient is booking appointment again")
    @GetMapping("/{appointId}")
    public ResponseEntity<GenericMessage>getFollowDetails(@PathVariable("appointId") Long appointId){
        log.info("AppointmentController::getFollowDetails");

        return appointmentService.getFollowDetails(appointId);
    }

}
