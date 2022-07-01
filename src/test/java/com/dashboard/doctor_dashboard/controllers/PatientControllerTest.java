package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.Patient;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.services.patient_service.PatientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PatientControllerTest {


    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

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
    void addPatientTest() {
        Long id = 1L;
        PatientEntityDto patientEntityDto = new PatientEntityDto(1L,"9728330045","Male",21,"A+","Address1","9728330045");
        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        Mockito.when(patientService.addPatient(patientEntityDto,id)).thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patient), HttpStatus.OK));

        ResponseEntity<GenericMessage> newPatient = patientController.addPatientDetails(patientEntityDto,id);
        assertThat(newPatient).isNotNull();
        assertEquals(patient.toString(),newPatient.getBody().getData().toString());
    }

    //    @Test
//    void getAllPatientsByDoctorId() {
//        final Long doctorId = 1L;
//        List<PatientListDto> list = new ArrayList<>();
//        PatientListDto patient1 = new PatientListDto();
//        PatientListDto patient2 = new PatientListDto();
//        patient1.setAge(21);
//        patient1.setCategory("orthology");
//        patient1.setEmailId("sagarssn23@gmail.com");
//        patient1.setFullName("Sagar Singh Negi");
//        patient1.setMobileNo("900011112");
//        patient1.setPID(1L);
//        patient1.setGender("male");
//        patient1.setLastVisitedDate(null);
//        patient1.setStatus("Active");
//
//        patient2.setAge(21);
//        patient2.setCategory("orthology");
//        patient2.setEmailId("sagarssn23@gmail.com");
//        patient2.setFullName("Sagar Singh Negi");
//        patient2.setMobileNo("900011112");
//        patient2.setPID(2L);
//        patient2.setGender("male");
//        patient2.setLastVisitedDate(null);
//        patient2.setStatus("Active");
//
//        list.addAll(Arrays.asList(patient1,patient2));
//
//        Mockito.when(patientService.getAllPatientByDoctorId(doctorId)).thenReturn(list);
//
//        List<PatientListDto> newList = patientController.getAllPatientsByDoctorId(doctorId);
//
//        assertThat(newList).isNotNull();
//        assertEquals(newList.size(),list.size());
//        assertEquals(newList.get(0).getPID(),patient1.getPID());
//        assertEquals(newList.get(1).getPID(),patient2.getPID());
//        assertEquals(newList.get(1).getCategory(),patient2.getCategory());
//
//    }
//
//    @Test
//    void getPatientDtoById() {
//        final Long id = 1L;
//        final Long doctorId = 1L;
//        PatientDto patient = new PatientDto();
//        patient.setAge(21);
//        patient.setCategory("orthology");
//        patient.setEmailId("sagarssn23@gmail.com");
//        patient.setFullName("Sagar Singh Negi");
//        patient.setMobileNo("900011112");
//        patient.setPID(id);
//        patient.setGender("male");
//        patient.setLastVisitedDate(null);
//        patient.setStatus("Active");
//        patient.setAttributes(null);
//
//        Mockito.when(patientService.getPatientById(id,doctorId)).thenReturn(patient);
//
//        PatientDto newPatient = patientController.getPatientDtoById(id,doctorId);
//
//        assertThat(newPatient).isNotNull();
//        assertEquals(newPatient.getPID(),patient.getPID());
//        assertEquals(newPatient.getFullName(),patient.getFullName());
//
//
//
//    }
//
    @Test
    void getAppointmentViewByAppointmentIdTest() {
        Long appointmentId = 1L;
        Long patientId = 1L;

        AppointmentViewDto viewDto = new AppointmentViewDto("Sagar","genral",LocalDate.now(),LocalTime.now(),"completed","B+", (short) 21,"Male");


        Mockito.when(patientService.viewAppointment(Mockito.any(Long.class),Mockito.any(Long.class)))
                .thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,viewDto), HttpStatus.OK));


        ResponseEntity<GenericMessage> appointmentView = patientController.getAppointmentViewByAppointmentId(appointmentId,patientId);
        assertThat(appointmentView).isNotNull();
        assertEquals(viewDto.toString(),appointmentView.getBody().getData().toString());

    }


    @Test
    void patientProfileTest() {
        Long loginId = 1L;
        PatientEntityDto patientEntityDto = new PatientEntityDto(1L,"9728330045","Male",21,"A+","Address1","9728330045");

        Mockito.when(patientService.getPatientDetailsById(Mockito.any(Long.class)))
                .thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patientEntityDto),HttpStatus.OK));

        ResponseEntity<GenericMessage> profileView = patientController.patientProfile(loginId);
        assertThat(profileView).isNotNull();
        assertEquals(patientEntityDto.toString(),profileView.getBody().getData().toString());

    }





    @Test
    void updatePatientTest() {
        final Long id = 1L;
        String message = "Mobile No. Successfully Updated";

        PatientDetailsUpdateDto updateDto = new PatientDetailsUpdateDto(id,"9728330045");

        Mockito.when(patientService.updatePatientDetails(Mockito.any(Long.class),Mockito.any(PatientDetailsUpdateDto.class))).thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,message),HttpStatus.OK));

        ResponseEntity<GenericMessage> newPatient = patientController.updatePatientDetails(id,updateDto);
        assertThat(newPatient).isNotNull();
        assertEquals(message,newPatient.getBody().getData().toString());

    }

    @Test
    void deletePatientByIdTest() {
        Long id = 1L;

        patientController.deletePatientById(id);
        patientController.deletePatientById(id);

        verify(patientService,times(2)).deletePatientById(id);
    }

    @Test
    void getNotifications() {
        Long appointmentId = 1L;
        NotificationDto notificationDto = new NotificationDto(appointmentId,"Sagar");

        Mockito.when(patientService.getNotifications(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,notificationDto),HttpStatus.OK));

        ResponseEntity<GenericMessage> notification = patientController.getNotifications(appointmentId);
        assertThat(notification).isNotNull();
        assertEquals(notificationDto.toString(),notification.getBody().getData().toString());
    }


//
//
//    @Test
//    void changePatientStatus() {
//        Long id = 1L;
//        StatusDto statusDto = new StatusDto();
//        statusDto.setStatus("Active");
//
//        patientController.changePatientStatus(id,statusDto);
//        patientController.changePatientStatus(id,statusDto);
//
//        verify(patientService,times(2)).changePatientStatus(id,statusDto.getStatus());
//    }
//
//    @Test
//    void recentlyAddedPatient() {
//        final Long doctorId = 1L;
//        List<PatientListDto> list = new ArrayList<>();
//        PatientListDto patient1 = new PatientListDto();
//        PatientListDto patient2 = new PatientListDto();
//        patient1.setAge(21);
//        patient1.setCategory("orthology");
//        patient1.setEmailId("sagarssn23@gmail.com");
//        patient1.setFullName("Sagar Singh Negi");
//        patient1.setMobileNo("900011112");
//        patient1.setPID(1L);
//        patient1.setGender("male");
//        patient1.setLastVisitedDate(null);
//        patient1.setStatus("Active");
//
//        patient2.setAge(21);
//        patient2.setCategory("orthology");
//        patient2.setEmailId("sagarssn23@gmail.com");
//        patient2.setFullName("Sagar Singh Negi");
//        patient2.setMobileNo("900011112");
//        patient2.setPID(2L);
//        patient2.setGender("male");
//        patient2.setLastVisitedDate(null);
//        patient2.setStatus("Active");
//
//        list.addAll(Arrays.asList(patient1,patient2));
//
//        Mockito.when(patientService.recentlyAddedPatient(doctorId)).thenReturn(list);
//
//        List<PatientListDto> newList = patientController.recentlyAddedPatient(doctorId);
//
//        assertThat(newList).isNotNull();
//        assertEquals(newList.size(),list.size());
//        assertEquals(newList.get(0).getPID(),patient1.getPID());
//        assertEquals(newList.get(1).getPID(),patient2.getPID());
//
//    }
//
//    @Test
//    void referPatients() {
//        final Long id = 1L;
//        final Long doctorId = 1L;
//
//        String value = "Patient Referred";
//        Mockito.when(patientService.referPatients(doctorId,id)).thenReturn(value);
//
//        String response = patientController.referPatients(doctorId,id);
//        assertThat(response).isNotNull();
//        assertEquals(response,value);
//
//    }
//
//    @Test
//    void getMessageForReferredPatient() {
//        final Long doctorId = 1L;
//        ArrayList<String> list = new ArrayList<>();
//
//        String message1 = "Patient1 referred";
//        String message2 = "Patient2 referred";
//        list.addAll(Arrays.asList(message1,message2));
//
//        Mockito.when(patientService.getMessageForReferredPatient(doctorId)).thenReturn(list);
//
//        List<String> newList = patientController.getMessageForReferredPatient(doctorId);
//
//        assertThat(newList).isNotNull();
//        assertEquals(newList.size(),list.size());
//        assertEquals(newList,list);
//    }
//
//    @Test
//    void changeStatus() {
//        final Long doctorId = 1L;
//
//        patientController.changeStatus(doctorId);
//        patientController.changeStatus(doctorId);
//
//        verify(patientService,times(2)).changeStatus(doctorId);
//    }
//
//
//
//    @Test
//    void totalPatient() {
//        final Long doctorId =1L;
//        int count = 10;
//
//        Mockito.when(patientService.totalNoOfPatient(doctorId)).thenReturn(count);
//
//        int newCount = patientController.totalPatient(doctorId);
//
//        assertEquals(newCount,count);
//
//    }
//
//    @Test
//    void totalNoOfPatientAddedThisWeek() {
//        final Long doctorId =1L;
//        int Active = 5;
//
//        Mockito.when(patientService.totalNoOfPatientAddedThisWeek(doctorId)).thenReturn(Active);
//
//        int newCount = patientController.totalNoOfPatientAddedThisWeek(doctorId);
//
//        assertEquals(newCount,Active);
//    }
//
//
//    @Test
//    void patientCategory() {
//        final Long doctorId = 1L;
//        ArrayList<String> list = new ArrayList<>();
//
//        String category1 = "orthology";
//        String category2 = "general";
//        list.addAll(Arrays.asList(category1,category2));
//
//        Mockito.when(patientService.patientCategory(doctorId)).thenReturn(list);
//
//        List<String> newList = patientController.patientCategory(doctorId);
//
//        assertThat(newList).isNotNull();
//        assertEquals(newList.size(),list.size());
//        assertEquals(newList,list);
//    }
//
//    @Test
//    void gender() {
//        final Long doctorId = 1L;
//        ArrayList<String> list = new ArrayList<>();
//
//        String gender1 = "Male";
//        String gender2 = "Female";
//        list.addAll(Arrays.asList(gender1,gender2));
//
//        Mockito.when(patientService.gender(doctorId)).thenReturn(list);
//
//        List<String> newList = patientController.gender(doctorId);
//
//        assertThat(newList).isNotNull();
//        assertEquals(newList.size(),list.size());
//        assertEquals(newList,list);
//    }
//
//    @Test
//    void activePatient() {
//
//        final Long doctorId = 1L;
//        ArrayList<String> list = new ArrayList<>();
//
//        String string1 = "Active";
//        String string2 = "Inactive";
//        list.addAll(Arrays.asList(string1,string2));
//
//        Mockito.when(patientService.weeklyPatientCountChart(doctorId)).thenReturn(list);
//
//        List<String> newList = patientController.weeklyPatientCountChart(doctorId);
//
//        assertThat(newList).isNotNull();
//        assertEquals(newList.size(),list.size());
//        assertEquals(newList,list);
//    }
//
//    @Test
//    void bloodGroup() {
//        final Long doctorId = 1L;
//        ArrayList<String> list = new ArrayList<>();
//
//        String bloodGroup1 = "A+";
//        String bloodGroup2 = "B+";
//        list.addAll(Arrays.asList(bloodGroup1,bloodGroup2));
//
//        Mockito.when(patientService.bloodGroup(doctorId)).thenReturn(list);
//
//        List<String> newList = patientController.bloodGroup(doctorId);
//
//        assertThat(newList).isNotNull();
//        assertEquals(newList.size(),list.size());
//        assertEquals(newList,list);
//    }
//
//    @Test
//    void ageChart() {
//        final Long doctorId = 1L;
//        ArrayList<String> list = new ArrayList<>();
//
//        String ageChart1 = "15-25";
//        String ageChart2 = "26-64";
//        list.addAll(Arrays.asList(ageChart1,ageChart2));
//
//        Mockito.when(patientService.ageChart(doctorId)).thenReturn(list);
//
//        List<String> newList = patientController.ageChart(doctorId);
//
//        assertThat(newList).isNotNull();
//        assertEquals(newList.size(),list.size());
//        assertEquals(newList,list);
//    }


}