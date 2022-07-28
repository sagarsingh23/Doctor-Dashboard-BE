package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.Prescription;
import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.dtos.PatientDto;
import com.dashboard.doctor_dashboard.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.services.PrescriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PrescriptionControllerTest {

    @Mock
    private PrescriptionService prescriptionService;

    @InjectMocks
    private PrescriptionController prescriptionController;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(prescriptionController).build();

        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }

    @Test
    @DisplayName("Add Prescription details")
    void addPrescriptionTest() throws Exception {
        final Long id =1L;
        PatientDto patientDto = new PatientDto("dentist","Dr.pranay","completed","sagar","sagar@gmail.com",21,"male");
        UpdatePrescriptionDto details = new UpdatePrescriptionDto(patientDto,null,"completed","mri check",true,1L);

        Mockito.when(prescriptionService.addPrescription(Mockito.any(Long.class),Mockito.any(UpdatePrescriptionDto.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,details), HttpStatus.CREATED));

        String content = objectMapper.writeValueAsString(details);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/prescription/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated());

    }

    @Test
    @DisplayName("View All Prescription")
    void getALlPrescriptionTest() throws Exception {
        final Long appointId = 1L;
        Prescription prescription1 = new Prescription(1L,"pcm",5L,"before food",5L,"morning",null,null,null);
        Prescription prescription2 = new Prescription(2L,"dolo",5L,"before food",5L,"morning",null,null,null);

        List<Prescription> prescriptions = new ArrayList<>(Arrays.asList(prescription1,prescription2));

        Mockito.when(prescriptionService.getAllPrescriptionByAppointment(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,prescriptions), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/prescription/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    @DisplayName("Delete Appointment")
    void deleteAppointmentTest() throws Exception {
        Long id = 1L;
        String message = "Successfully deleted";

        Mockito.when(prescriptionService.deleteAppointmentById(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,message), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/prescription/private/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());



    }
}