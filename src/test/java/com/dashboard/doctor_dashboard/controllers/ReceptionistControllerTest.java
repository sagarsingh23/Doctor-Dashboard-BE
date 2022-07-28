package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.AttributesDto;
import com.dashboard.doctor_dashboard.dtos.DoctorDropdownDto;
import com.dashboard.doctor_dashboard.dtos.PatientViewDto;
import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.enums.Category;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.services.ReceptionistService;
import com.dashboard.doctor_dashboard.utils.Constants;
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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    @DisplayName("View All Doctor Names")
    void getDoctorNamesTest() throws Exception {

        DoctorDropdownDto dto1 = new DoctorDropdownDto(1L,"Sagar","sagar@gmail.com", Category.Orthologist);
        DoctorDropdownDto dto2 = new DoctorDropdownDto(2L,"pranay","pranay@gmail.com",Category.Dentist);
        List<DoctorDropdownDto> list = new ArrayList<>(Arrays.asList(dto1, dto2));

        Mockito.when(receptionistService.getDoctorDetails()).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/receptionist/doctor-names").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    @DisplayName("View All Appointment By Doctor")
    void getAppointmentListTest() throws Exception {
        PatientViewDto dto1 = new PatientViewDto(1L, LocalTime.now(),"sagar","sagar@gmail.com","completed");
        PatientViewDto dto2 = new PatientViewDto(2L, LocalTime.now(),"pranay","pranay@gmail.com","follow up");
        List<PatientViewDto> list = new ArrayList<>(Arrays.asList(dto1, dto2));

        Mockito.when(receptionistService.getDoctorAppointments(Mockito.any(Long.class),Mockito.any(Integer.class),Mockito.any(Integer.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/receptionist/appointment-list/1?pageNo=0&pageSize=11").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    @DisplayName("Add Vitals")
    void getAddVitalsTest() throws Exception {
        String message = "Vital Successfully updated";

        AttributesDto attributes = new AttributesDto("120/80",100L,99D,"mri check",new Appointment());

        Mockito.when(receptionistService.addAppointmentVitals(Mockito.any(AttributesDto.class),Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,message), HttpStatus.CREATED));

        String content = objectMapper.writeValueAsString(attributes);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/receptionist/vitals/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated());

    }

    @Test
    @DisplayName("View Today's All Appointment For Clinic Staff")
    void getTodayAllAppointmentForClinicStaffTest() throws Exception {
        int pageNo = 1;
        PatientViewDto dto1 = new PatientViewDto(1L, LocalTime.now(),"sagar","sagar@gmail.com","completed");
        PatientViewDto dto2 = new PatientViewDto(2L, LocalTime.now(),"pranay","pranay@gmail.com","follow up");
        PatientViewDto dto3 = new PatientViewDto(3L, LocalTime.now(),"gokul","gokul@gmail.com"," complete");

        List<PatientViewDto> list = new ArrayList<>(Arrays.asList(dto1, dto2, dto3));

        Mockito.when(receptionistService.todayAllAppointmentForClinicStaff(Mockito.any(Integer.class),Mockito.any(Integer.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list), HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/receptionist/all-appointments?pageNo=0").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}