package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.AppointmentViewDto;
import com.dashboard.doctor_dashboard.dtos.NotificationDto;
import com.dashboard.doctor_dashboard.dtos.PatientEntityDto;
import com.dashboard.doctor_dashboard.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.entities.Patient;
import com.dashboard.doctor_dashboard.enums.BloodGroup;
import com.dashboard.doctor_dashboard.enums.Category;
import com.dashboard.doctor_dashboard.enums.Gender;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.services.PatientService;
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

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PatientControllerTest {


    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();

        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }


    @Test
    @DisplayName("Add Patient Details")
    void addPatientTest() throws Exception {
        Long id = 1L;
        PatientEntityDto patientEntityDto = new PatientEntityDto("1234567890",Gender.Male,21,BloodGroup.Apositive,"Address1","1234567890");
        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender(Gender.Male);
        patient.setBloodGroup(BloodGroup.Apositive);
        patient.setAlternateMobileNo("900011112");

        Mockito.when(patientService.addPatient(Mockito.any(PatientEntityDto.class),Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patient), HttpStatus.CREATED));

        String content = objectMapper.writeValueAsString(patientEntityDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/patient/on-boarding/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated());

    }

    @Test
    @DisplayName("View Appointment Details By Appointment Id")
    void getAppointmentViewByAppointmentIdTest() throws Exception {

        AppointmentViewDto viewDto = new AppointmentViewDto("Sagar", Category.General,LocalDate.now(),LocalTime.now(),"completed",BloodGroup.Apositive, (short) 21,Gender.Male);


        Mockito.when(patientService.viewAppointment(Mockito.any(Long.class),Mockito.any(Long.class)))
                .thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,viewDto), HttpStatus.OK));


        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/patient/1/appointment/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }


    @Test
    @DisplayName("View Patient Appointment Details")
    void patientProfileTest() throws Exception {

        PatientEntityDto patientEntityDto = new PatientEntityDto("1234567890",Gender.Male,21,BloodGroup.Apositive,"Address1","1234567890");

        Mockito.when(patientService.getPatientDetailsById(Mockito.any(Long.class)))
                .thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patientEntityDto),HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/patient/patient-profile/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }


    @Test
    @DisplayName("Update Patient Details")
    void updatePatientTest() throws Exception {
        final Long id = 1L;
        String message = "Mobile No. Successfully Updated";

        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto(id,"1234567890");

        Mockito.when(patientService.updatePatientDetails(Mockito.any(Long.class),Mockito.any(UserDetailsUpdateDto.class))).thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,message),HttpStatus.OK));

        String content = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/patient/update/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isOk());


    }

    @Test
    @DisplayName("Delete Patient Details")
    void deletePatientByIdTest() throws Exception {
        Long id = 1L;

        patientController.deletePatientById(id);
        patientController.deletePatientById(id);

        verify(patientService,times(2)).deletePatientById(id);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/patient/private/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    @DisplayName("View All Notifications By Patient Id")
    void getNotifications() throws Exception {
        Long appointmentId = 1L;
        NotificationDto notificationDto = new NotificationDto(appointmentId,"Sagar");

        Mockito.when(patientService.getNotifications(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,notificationDto),HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/patient/1/notifications").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}