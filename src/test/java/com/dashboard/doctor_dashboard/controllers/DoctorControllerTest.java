package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.entities.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorService;
import com.dashboard.doctor_dashboard.exceptions.ValidationsException;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {


    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;


    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(doctorController).build();

        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }


    @Test
    void  getAllDoctors() throws Exception {
        final Long id = 1L;
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthology",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthology",(short)6,"MBBS");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,list);


        Mockito.when(doctorService.getAllDoctors(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/get-all/doctor/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void throwErrorIfIdNotPresentDbForAllDoctor() throws Exception {
        final Long id = 1L;
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthology",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthology",(short)6,"MBBS");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        Mockito.when(doctorService.getAllDoctors(Mockito.any(Long.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/get-all/doctor/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());


    }

    @Test
    void getDoctorsByIdIfIdPresent() throws Exception {
        final Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21,"MBBS",(short)8);

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,doctorDetails);


        Mockito.when(doctorService.getDoctorById(id)).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/id/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void throwErrorIfIdNotPresentDb() throws Exception {
        final Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21,"MBBS",(short)8);

        Mockito.when(doctorService.getDoctorById(id)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/id/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());


    }

    @Test
    void addDoctorDetails() throws Exception {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"Orthologist","male",
                "9728330045",(short)6,"MBBS");
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,doctorFormDto);


        Mockito.when(doctorService.addDoctorDetails(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(new ResponseEntity<>(message,HttpStatus.CREATED));


        String content = objectMapper.writeValueAsString(doctorFormDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/doctor/add-doctor-details/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated());

    }

    @Test
    void checkIfAddDoctorDetailsHasError() throws Exception {
        BindingResult result = mock(BindingResult.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        ObjectError error = new ObjectError("age","age should be between 24-100");
        result.addError(error);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");


        String content = objectMapper.writeValueAsString(doctorFormDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/doctor/add-doctor-details/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isUnprocessableEntity());


    }

    @Test
    void throwErrorIfIdMisMatchForAddDoctor() throws Exception {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);


        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"Orthologist","male",
                "9728330045",(short)6,"MBBS");

        Mockito.when(doctorService.addDoctorDetails(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(null);

        String content = objectMapper.writeValueAsString(doctorFormDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/doctor/add-doctor-details/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isMethodNotAllowed());


    }



    @Test
    void updateDoctorDetails() throws Exception {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"Orthologist","male",
                "9728330045",(short)6,"MBBS");

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,doctorFormDto);


        Mockito.when(doctorService.updateDoctor(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));


        String content = objectMapper.writeValueAsString(doctorFormDto);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/doctor/update/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isOk());

    }

    @Test
    void checkIfUpdateDoctorDetailsHasError() throws Exception {
        BindingResult result = mock(BindingResult.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        ObjectError error = new ObjectError("age","age should be between 24-100");
        result.addError(error);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");


        String content = objectMapper.writeValueAsString(doctorFormDto);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/doctor/update/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isUnprocessableEntity());


    }

    @Test
    void throwErrorIfIdMisMatchForUpdateDoctor() throws Exception {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);


        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"Orthologist","male",
                "9728330045",(short)6,"MBBS");

        Mockito.when(doctorService.updateDoctor(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(null);

        String content = objectMapper.writeValueAsString(doctorFormDto);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/doctor/update/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isMethodNotAllowed());


    }

    @Test
    void deleteDoctor() throws Exception {
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,"Deleted");

        Mockito.when(doctorService.deleteDoctor(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/doctor/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void getAllDoctorsBySpecialityTest() throws Exception {
        final String speciality = "orthology";
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthology",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthology",(short)6,"MBBS");
        List<DoctorListDto> list = new ArrayList<DoctorListDto>(Arrays.asList(doctorListDto1, doctorListDto2));

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,list);


        Mockito.when(doctorService.getAllDoctorsBySpeciality(Mockito.any(String.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/get-all-doctor/orthology").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void genderChartTest() throws Exception {

        final Long id = 1L;
        Map<String,Integer> m = new HashMap<>();
        m.put("Male",2);
        m.put("Female",5);

        GenericMessage message = new GenericMessage(Constants.SUCCESS,m);

        Mockito.when(doctorService.genderChart(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/1/gender").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());



    }

    @Test
    void bloodGroupChartTest() throws Exception {

        final Long id = 1L;
        Map<String,Integer> m = new HashMap<>();
        m.put("A+",2);
        m.put("AB-",5);

        GenericMessage message = new GenericMessage(Constants.SUCCESS,m);

        Mockito.when(doctorService.bloodGroupChart(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/1/bloodGroup").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    void ageGroupChartTest() throws Exception {

        final Long id = 1L;
        Map<String,Integer> m = new HashMap<>();
        m.put("0-2",1);
        m.put("3-14",2);
        m.put("26-64",7);


        GenericMessage message = new GenericMessage(Constants.SUCCESS,m);

        Mockito.when(doctorService.ageGroupChart(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/doctor/1/ageGroup").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }
}