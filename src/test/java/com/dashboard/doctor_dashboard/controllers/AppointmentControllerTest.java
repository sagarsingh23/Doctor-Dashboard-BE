package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.model.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.entities.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.services.appointment_service.AppointmentService;
import org.codehaus.jettison.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

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
    void addAppointmentTest() throws MessagingException, JSONException, UnsupportedEncodingException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Map<String,String> m = new HashMap<>();
        m.put("appointId","1L");
        m.put("message","Successfully created");

        AppointmentDto appointment = new AppointmentDto(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,null);

        Mockito.when(appointmentService.addAppointment(Mockito.any(AppointmentDto.class),Mockito.any(HttpServletRequest.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,m), HttpStatus.OK));

        ResponseEntity<GenericMessage> response = appointmentController.addAppointment(appointment,request);
        assertThat(response).isNotNull();
        assertEquals(m, Objects.requireNonNull(response.getBody()).getData());
    }

    @Test
    void showAvailableSlotsTest() {
        String date = "2022-06-28";
        final Long id = 1L;
        final List<Boolean> timesSlots=List.of(true,true,true,true,true,true,true,true,true,true,true,true);

        Mockito.when(appointmentService.checkSlots(Mockito.any(LocalDate.class),Mockito.any(Long.class))).thenReturn(timesSlots);

        ResponseEntity<GenericMessage> slots = appointmentController.showAvailableSlots(date,id);
        assertThat(slots).isNotNull();
        assertEquals(timesSlots,slots.getBody().getData());
    }

    @Test
    void getAllAppointmentByPatientIdTest() {
        final Long patientId = 1L;
        Map<String, List<PatientAppointmentListDto>> map =new HashMap<>();
        PatientAppointmentListDto dto1 = new PatientAppointmentListDto(2L,"dentist", LocalDate.now(),LocalTime.now(),"sagar","completed",true);
        PatientAppointmentListDto dto2 = new PatientAppointmentListDto(1L,"dentist", LocalDate.now(),LocalTime.now(),"sagar","completed",true);

        List<PatientAppointmentListDto> dto = new ArrayList<>(Arrays.asList(dto1,dto2));
        map.put("past",dto);
        map.put("today",dto);
        map.put("upcoming",dto);

        Mockito.when(appointmentService.getAllAppointmentByPatientId(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,map), HttpStatus.OK));

        ResponseEntity<GenericMessage> newDto = appointmentController.getAllAppointmentByPatientId(patientId);
        assertThat(newDto).isNotNull();
        assertEquals(map,newDto.getBody().getData());

    }

    @Test
    void getAllAppointmentByDoctorIdTest() {
        final Long doctorId = 1L;
        Map<String, List<DoctorAppointmentListDto>> map =new HashMap<>();
        DoctorAppointmentListDto dto1 = new DoctorAppointmentListDto(2L, LocalDate.now(),"sagar","sagarssn23@gmal.com","completed",LocalTime.now());
        DoctorAppointmentListDto dto2 = new DoctorAppointmentListDto(3L, LocalDate.now(),"sagar","sagarssn23@gmal.com","completed",LocalTime.now());
        List<DoctorAppointmentListDto> dto = new ArrayList<>(Arrays.asList(dto1,dto2));
        map.put("past",dto);
        map.put("today",dto);
        map.put("upcoming",dto);

        Mockito.when(appointmentService.getAllAppointmentByDoctorId(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,map), HttpStatus.OK));

        ResponseEntity<GenericMessage> newDto = appointmentController.getAllAppointmentByDoctorId(doctorId);
        assertThat(newDto).isNotNull();
        assertEquals(map,newDto.getBody().getData());
    }

    @Test
    void getTotalNoOfAppointmentTest() {
        final Long doctorId = 1L;
        int totalNoOfAppointment = 4;

        Mockito.when(appointmentService.totalNoOfAppointment(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,totalNoOfAppointment), HttpStatus.OK));

        ResponseEntity<GenericMessage> newCount = appointmentController.totalNoOfAppointment(doctorId);
        assertThat(newCount).isNotNull();
        assertEquals(totalNoOfAppointment,newCount.getBody().getData());
    }

    @Test
    void getTodayAppointmentsTest() {
        final Long doctorId = 1L;
        int totalAppointmentToday = 2;

        Mockito.when(appointmentService.todayAppointments(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,totalAppointmentToday), HttpStatus.OK));

        ResponseEntity<GenericMessage> newCount = appointmentController.todayAppointments(doctorId);
        assertThat(newCount).isNotNull();
        assertEquals(totalAppointmentToday,newCount.getBody().getData());
    }

    @Test
    void getTotalNoOfAppointmentAddedThisWeekTest() {
        final Long doctorId = 1L;
        int totalAppointmentThisWeek = 10;

        Mockito.when(appointmentService.totalNoOfAppointmentAddedThisWeek(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,totalAppointmentThisWeek), HttpStatus.OK));

        ResponseEntity<GenericMessage> newCount = appointmentController.totalNoOfAppointmentAddedThisWeek(doctorId);
        assertThat(newCount).isNotNull();
        assertEquals(totalAppointmentThisWeek,newCount.getBody().getData());
    }

    @Test
    void getPatientCategoryGraphTest() {
        Long patientId = 1L;
        List<String> charts = new ArrayList<>();
        charts.add("dentist,4");
        charts.add("gastrologist,5");

        Mockito.when(appointmentService.patientCategoryGraph(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,charts), HttpStatus.OK));

        ResponseEntity<GenericMessage> newCharts = appointmentController.patientCategoryGraph(patientId);
        assertThat(newCharts).isNotNull();
        assertEquals(charts,newCharts.getBody().getData());
    }

    @Test
    void getFollowDetailsTest() {
        final Long appointId = 1L;
        FollowUpDto followUpDto = new FollowUpDto(1L,"sagar",1L,"dentist","completed");

        Mockito.when(appointmentService.getFollowDetails(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,followUpDto), HttpStatus.OK));

        ResponseEntity<GenericMessage> newDetails = appointmentController.getFollowDetails(appointId);
        assertThat(newDetails).isNotNull();
        assertEquals(followUpDto,newDetails.getBody().getData());
    }

    @Test
    void weeklyDoctorCountChart() {
        final Long doctorId = 1L;

        List<String> chartsDoctor = new ArrayList<>();
        chartsDoctor.add("week1,1");
        chartsDoctor.add("week2,2");
        chartsDoctor.add("week3,3");
        chartsDoctor.add("week4,4");
        chartsDoctor.add("week5,5");

        Mockito.when(appointmentService.weeklyDoctorCountChart(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chartsDoctor), HttpStatus.OK));

        ResponseEntity<GenericMessage> newChart = appointmentController.weeklyDoctorCountChart(doctorId);
        assertThat(newChart).isNotNull();
        assertEquals(chartsDoctor,newChart.getBody().getData());

    }

    @Test
    void weeklyPatientCountChart() {
        final Long patientId = 1L;

        List<String> chartsPatient = new ArrayList<>();
        chartsPatient.add("week1,2");
        chartsPatient.add("week2,3");
        chartsPatient.add("week3,4");
        chartsPatient.add("week4,5");
        chartsPatient.add("week5,1");

        Mockito.when(appointmentService.weeklyPatientCountChart(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chartsPatient), HttpStatus.OK));

        ResponseEntity<GenericMessage> newChart = appointmentController.weeklyPatientCountChart(patientId);
        assertThat(newChart).isNotNull();
        assertEquals(chartsPatient,newChart.getBody().getData());
    }

    @Test
    void recentAppointment() {
        final Long doctorId = 1L;

        DoctorAppointmentListDto dto1 = new DoctorAppointmentListDto(2L, LocalDate.now(),"sagar","sagarssn23@gmal.com","completed",LocalTime.now());
        DoctorAppointmentListDto dto2 = new DoctorAppointmentListDto(3L, LocalDate.now(),"sagar","sagarssn23@gmal.com","completed",LocalTime.now());
        List<DoctorAppointmentListDto> dto = new ArrayList<>(Arrays.asList(dto1,dto2));

        Mockito.when(appointmentService.recentAppointment(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,dto), HttpStatus.OK));

        ResponseEntity<GenericMessage> newDto = appointmentController.recentAppointment(doctorId);
        assertThat(newDto).isNotNull();
        assertEquals(dto,newDto.getBody().getData());
    }

    @Test
    void getAppointmentByIdTest() {
        final Long appointId = 1L;

        PatientProfileDto patientProfileDto = new PatientProfileDto(1L,LocalDate.now(),"sagar","sagarssn3@gmail.com",
                "fever","dentist",true,1L,null,null,null,"completed");

        Mockito.when(appointmentService.getAppointmentById(Mockito.any(Long.class))).thenReturn(
                new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patientProfileDto), HttpStatus.OK));

        ResponseEntity<GenericMessage> newDetails = appointmentController.getAppointmentById(appointId);
        assertThat(newDetails).isNotNull();
        assertEquals(patientProfileDto,newDetails.getBody().getData());

    }


}