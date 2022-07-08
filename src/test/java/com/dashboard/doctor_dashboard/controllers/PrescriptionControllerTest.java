package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.model.Prescription;
import com.dashboard.doctor_dashboard.Utils.Constants;
import com.dashboard.doctor_dashboard.Utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entities.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.services.prescription_service.PrescriptionService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
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
    void addPrescriptionTest() throws Exception {
        final Long id =1L;
        PatientDto patientDto = new PatientDto("dentist","Dr.pranay","completed","sagar","sagarssn23@gmail.com",21,"male");
        UpdatePrescriptionDto details = new UpdatePrescriptionDto(patientDto,null,"completed","mri check",true,1L);

        Mockito.when(prescriptionService.addPrescription(Mockito.any(Long.class),Mockito.any(UpdatePrescriptionDto.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,details), HttpStatus.CREATED));

        String content = objectMapper.writeValueAsString(details);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/prescription/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated());

    }

    @Test
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
    void deleteAppointmentTest() throws Exception {
        Long id = 1L;
        String message = "Successfully deleted";

        Mockito.when(prescriptionService.deleteAppointmentById(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,message), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/prescription/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());



    }
}