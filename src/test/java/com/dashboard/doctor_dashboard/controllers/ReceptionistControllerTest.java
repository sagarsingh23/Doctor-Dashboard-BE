package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.Utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.services.receptionist.ReceptionistService;
import com.dashboard.doctor_dashboard.Utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReceptionistControllerTest {

    @Mock
    private ReceptionistService receptionistService;

    MockMvc mockMvc;

    @InjectMocks
    private ReceptionistController receptionistController;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(receptionistController).build();

        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }

    @Test
    void getDoctorNamesTest() throws Exception {

        DoctorDropdownDto dto1 = new DoctorDropdownDto(1L,"Sagar","sagarssn23@gmail.com","orthology");
        DoctorDropdownDto dto2 = new DoctorDropdownDto(2L,"pranay","pranay@gmail.com","dentist");
        List<DoctorDropdownDto> list = new ArrayList<>(Arrays.asList(dto1, dto2));

        Mockito.when(receptionistService.getDoctorDetails()).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/receptionist/doctorNames").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void getAppointmentListTest() throws Exception {
        PatientViewDto dto1 = new PatientViewDto(1L, LocalTime.now(),"sagar","sagarssn23@gmail.com","completed");
        PatientViewDto dto2 = new PatientViewDto(2L, LocalTime.now(),"pranay","pranay@gmail.com","follow up");
        List<PatientViewDto> list = new ArrayList<>(Arrays.asList(dto1, dto2));

        Mockito.when(receptionistService.getDoctorAppointments(Mockito.any(Long.class),Mockito.any(Integer.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/receptionist/appointmentList/1?pageNo=0").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void getAddVitalsTest() throws Exception {
        String message = "Vital Successfully updated";

        AttributesDto attributes = new AttributesDto(1L,"120/80",100L,99D,"mri check",null);

        Mockito.when(receptionistService.addAppointmentVitals(Mockito.any(AttributesDto.class),Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,message), HttpStatus.CREATED));

        String content = objectMapper.writeValueAsString(attributes);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/receptionist/addVitals/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated());

    }

    @Test
    void getTodayAllAppointmentForClinicStaffTest() throws Exception {
        int pageNo = 1;
        PatientViewDto dto1 = new PatientViewDto(1L, LocalTime.now(),"sagar","sagarssn23@gmail.com","completed");
        PatientViewDto dto2 = new PatientViewDto(2L, LocalTime.now(),"pranay","pranay@gmail.com","follow up");
        PatientViewDto dto3 = new PatientViewDto(3L, LocalTime.now(),"gokul","gokul@gmail.com"," complete");

        List<PatientViewDto> list = new ArrayList<>(Arrays.asList(dto1, dto2, dto3));

        Mockito.when(receptionistService.todayAllAppointmentForClinicStaff(Mockito.any(Integer.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list), HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/receptionist/getAllAppointments?pageNo=0").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}