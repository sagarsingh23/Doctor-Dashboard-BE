package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.services.receptionist.ReceptionistService;
import com.dashboard.doctor_dashboard.services.todo_service.TodoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReceptionistControllerTest {

    @Mock
    private ReceptionistService receptionistService;

    @InjectMocks
    private ReceptionistController receptionistController;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }

    @Test
    void getDoctorNamesTest() {

        DoctorDropdownDto dto1 = new DoctorDropdownDto(1L,"Sagar","sagarssn23@gmail.com","orthology");
        DoctorDropdownDto dto2 = new DoctorDropdownDto(2L,"pranay","pranay@gmail.com","dentist");
        List<DoctorDropdownDto> list = new ArrayList<>(Arrays.asList(dto1, dto2));

        Mockito.when(receptionistService.getDoctorDetails()).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list), HttpStatus.OK));

        ResponseEntity<GenericMessage> newList = receptionistController.doctorNames();
        assertThat(newList).isNotNull();
        assertEquals(list,newList.getBody().getData());

    }

    @Test
    void getAppointmentListTest() {
        final Long doctorId = 1L;
        PatientViewDto dto1 = new PatientViewDto(1L, LocalTime.now(),"sagar","sagarssn23@gmail.com","completed");
        PatientViewDto dto2 = new PatientViewDto(2L, LocalTime.now(),"pranay","pranay@gmail.com","follow up");
        List<PatientViewDto> list = new ArrayList<>(Arrays.asList(dto1, dto2));

        Mockito.when(receptionistService.getDoctorAppointments(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list), HttpStatus.OK));

        ResponseEntity<GenericMessage> newList = receptionistController.appointmentList(doctorId);
        assertThat(newList).isNotNull();
        assertEquals(list,newList.getBody().getData());

    }

    @Test
    void getAddVitalsTest() {
        final Long appointId = 1L;
        String message = "Vital Successfully updated";
        Attributes attributes = new Attributes(1L,"120/80",100L,99D,"mri check",null,null,null);

        Mockito.when(receptionistService.addAppointmentVitals(Mockito.any(Attributes.class),Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,message), HttpStatus.OK));

        ResponseEntity<GenericMessage> newMessage = receptionistController.addVitals(appointId,attributes);
        assertThat(newMessage).isNotNull();
        assertEquals(message,newMessage.getBody().getData());

    }

    @Test
    void getTodayAllAppointmentForClinicStaffTest() {

        PatientViewDto dto1 = new PatientViewDto(1L, LocalTime.now(),"sagar","sagarssn23@gmail.com","completed");
        PatientViewDto dto2 = new PatientViewDto(2L, LocalTime.now(),"pranay","pranay@gmail.com","follow up");
        PatientViewDto dto3 = new PatientViewDto(3L, LocalTime.now(),"gokul","gokul@gmail.com"," complete");

        List<PatientViewDto> list = new ArrayList<>(Arrays.asList(dto1, dto2, dto3));

        Mockito.when(receptionistService.todayAllAppointmentForClinicStaff()).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list), HttpStatus.OK));


        ResponseEntity<GenericMessage> newList = receptionistController.todayAllAppointmentForClinicStaff();
        assertThat(newList).isNotNull();
        assertEquals(list,newList.getBody().getData());
    }
}