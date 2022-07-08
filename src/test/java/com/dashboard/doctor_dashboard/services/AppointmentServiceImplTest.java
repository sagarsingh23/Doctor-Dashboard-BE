package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.entities.login_entity.LoginDetails;
import com.dashboard.doctor_dashboard.entities.model.Appointment;
import com.dashboard.doctor_dashboard.entities.model.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.model.Patient;
import com.dashboard.doctor_dashboard.entities.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.InvalidDate;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.exceptions.ValidationsException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import com.dashboard.doctor_dashboard.services.appointment_service.AppointmentServiceImpl;
import org.codehaus.jettison.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.management.LockInfo;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;


class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private LoginRepo loginRepo;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MailService mailService;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private ModelMapper mapper;

    @Mock
    private PdFGeneratorServiceImpl pdFGeneratorService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }


    @Test
    void addAppointment_SUCCESS() throws MessagingException, JSONException, UnsupportedEncodingException {

        Long loginId = 1L;
        Long patientId = 1L;
        Long doctorId = 4L;

        LocalDate localDate = LocalDate.of(2022,07,12);
        LocalTime localTime = LocalTime.of(10,30);

        Map<String,String> expected = new HashMap<>();
        expected.put("appointId","1");
        expected.put("message",Constants.APPOINTMENT_CREATED);


        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(4L);

        Appointment appointment1 = new Appointment(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmail.com",
                "pranay", localTime,true,"completed",null,null,null,null,2L,patient,doctorDetails,null,null);

        AppointmentDto appointment = new AppointmentDto(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",null,null,patient,doctorDetails);



        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(loginId);
        Mockito.when(loginRepo.isIdAvailable(loginId)).thenReturn(loginId);
        Mockito.when(patientRepository.getId(appointment.getPatient().getPID())).thenReturn(patientId);
        Mockito.when(doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId())).thenReturn(doctorId);
        Mockito.when(mapper.map(appointment,Appointment.class)).thenReturn(appointment1);
        Mockito.when(pdFGeneratorService.dateHandler(Mockito.any(LocalDate.class))).thenReturn(true);
        Mockito.doReturn(appointment1).when(appointmentRepository).save(Mockito.any(Appointment.class));
        Mockito.doNothing().when(mailService).mailServiceHandler(anyString(),anyString(),anyString(),anyString(),anyString());
        Mockito.doNothing().when(mailSender).send((MimeMessage) any());
        Mockito.doReturn(mimeMessage).when(mailSender).createMimeMessage();
        Mockito.when(loginRepo.email(appointment.getDoctorDetails().getId())).thenReturn("pranay23@gmail.com");
        Mockito.when(pdFGeneratorService.formatDate(appointment.getDateOfAppointment().toString())).thenReturn("12-07-2022");

        ResponseEntity<GenericMessage> response = appointmentService.addAppointment(appointment,request);
        System.out.println(response);

        assertThat(response).isNotNull();
        assertEquals(expected,response.getBody().getData());

    }



    @Test
    void addAppointment_ThrowErrorIfDateProvidedIsNotInOneWeekRange() throws MessagingException, JSONException, UnsupportedEncodingException {

        Long loginId = 1L;
        Long patientId = 1L;
        Long doctorId = 4L;

        LocalDate localDate = LocalDate.of(2022,07,16);
        LocalDate localDate1 = LocalDate.of(2022,07,04);

        LocalTime localTime = LocalTime.of(10,30);

        Map<String,String> expected = new HashMap<>();
        expected.put("appointId","1");
        expected.put("message",Constants.APPOINTMENT_CREATED);


        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(4L);

        Appointment appointment = new Appointment(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmail.com",
                "pranay", localTime,true,"completed",null,null,null,null,2L,patient,doctorDetails,null,null);

        AppointmentDto appointment1 = new AppointmentDto(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",null,2L,patient,doctorDetails);

        AppointmentDto appointment2 = new AppointmentDto(1L,"dentist", localDate1,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",null,2L,patient,doctorDetails);



        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(loginId);
        Mockito.when(loginRepo.isIdAvailable(loginId)).thenReturn(loginId);
        Mockito.when(patientRepository.getId(Mockito.any(Long.class))).thenReturn(patientId);
        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(doctorId);
        Mockito.when(mapper.map(appointment1,Appointment.class)).thenReturn(appointment);
        Mockito.when(mapper.map(appointment2,Appointment.class)).thenReturn(appointment);


        InvalidDate resourceNotFoundException = assertThrows(InvalidDate.class,()->{
            appointmentService.addAppointment(appointment1,request);
        });

        InvalidDate resourceNotFoundException1 = assertThrows(InvalidDate.class,()->{
            appointmentService.addAppointment(appointment2,request);
        });

        assertAll(
                ()-> assertThat(resourceNotFoundException).isNotNull(),
                ()-> assertThat(resourceNotFoundException1).isNotNull()
        );

        assertAll(
                ()-> assertEquals(localDate+":"+Constants.APPOINTMENT_CANNOT_BE_BOOKED,resourceNotFoundException.getMessage()),
                ()-> assertEquals(localDate1+":"+Constants.APPOINTMENT_CANNOT_BE_BOOKED,resourceNotFoundException1.getMessage())
        );
    }



    @Test
    void addAppointment_LoginIdNotPresentInLoginDb() throws MessagingException, JSONException, UnsupportedEncodingException {

        Long loginId = 1L;

        LocalDate localDate = LocalDate.of(2022,07,12);
        LocalTime localTime = LocalTime.of(8,30);

        Map<String,String> expected = new HashMap<>();
        expected.put("appointId","1");
        expected.put("message",Constants.APPOINTMENT_CREATED);


        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(4L);

//        Appointment appointment = new Appointment(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmail.com",
//                "pranay", localTime,true,"completed",null,null,null,null,2L,patient,doctorDetails,null,null);

        AppointmentDto appointment1 = new AppointmentDto(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",null,2L,patient,doctorDetails);



        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(loginId);
        Mockito.when(loginRepo.isIdAvailable(loginId)).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,()->{
            appointmentService.addAppointment(appointment1,request);
        });

        assertThat(resourceNotFoundException).isNotNull();
        assertEquals(Constants.LOGIN_DETAILS_NOT_FOUND,resourceNotFoundException.getMessage());

    }



    @Test
    void addAppointment_throwErrorIfPatientNotPresentInPatientDb() throws MessagingException, JSONException, UnsupportedEncodingException {

        Long loginId = 1L;
        Long patientId = 1L;
        Long doctorId = 4L;

        LocalDate localDate = LocalDate.of(2022,07,12);
        LocalTime localTime = LocalTime.of(8,30);

        Map<String,String> expected = new HashMap<>();
        expected.put("appointId","1");
        expected.put("message",Constants.APPOINTMENT_CREATED);


        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(4L);

        AppointmentDto appointment1 = new AppointmentDto(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",null,2L,patient,doctorDetails);

        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(loginId);
        Mockito.when(loginRepo.isIdAvailable(loginId)).thenReturn(loginId);
        Mockito.when(patientRepository.getId(Mockito.any(Long.class))).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,()->{
            appointmentService.addAppointment(appointment1,request);
        });

        assertThat(resourceNotFoundException).isNotNull();
        assertEquals(Constants.PATIENT_NOT_FOUND,resourceNotFoundException.getMessage());

    }

    @Test
    void addAppointment_throwErrorIfDoctorNotPresentInDoctorDb() throws MessagingException, JSONException, UnsupportedEncodingException {

        Long loginId = 1L;
        Long patientId = 1L;
        Long doctorId = 4L;

        LocalDate localDate = LocalDate.of(2022,07,12);
        LocalTime localTime = LocalTime.of(8,30);

        Map<String,String> expected = new HashMap<>();
        expected.put("appointId","1");
        expected.put("message",Constants.APPOINTMENT_CREATED);


        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(4L);

        AppointmentDto appointment1 = new AppointmentDto(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",null,2L,patient,doctorDetails);


        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(loginId);
        Mockito.when(loginRepo.isIdAvailable(loginId)).thenReturn(loginId);
        Mockito.when(patientRepository.getId(Mockito.any(Long.class))).thenReturn(patientId);
        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(null);


        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,()->{
            appointmentService.addAppointment(appointment1,request);
        });

        assertThat(resourceNotFoundException).isNotNull();
        assertEquals(Constants.DOCTOR_NOT_FOUND,resourceNotFoundException.getMessage());

    }




    @Test
    void addAppointment_IsBookedAgainHaveNullValueAndAppointmentTimeIsInvalid() throws MessagingException, JSONException, UnsupportedEncodingException {

        Long loginId = 1L;
        Long patientId = 1L;
        Long doctorId = 4L;

        LocalDate localDate = LocalDate.of(2022,07,12);
        LocalTime localTime = LocalTime.of(8,30);

        Map<String,String> expected = new HashMap<>();
        expected.put("appointId","1");
        expected.put("message",Constants.APPOINTMENT_CREATED);


        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(4L);

        Appointment appointment1 = new Appointment(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmail.com",
                "pranay", localTime,true,"completed",null,null,null,null,2L,patient,doctorDetails,null,null);

        AppointmentDto appointment = new AppointmentDto(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",null,2L,patient,doctorDetails);



        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(loginId);
        Mockito.when(loginRepo.isIdAvailable(loginId)).thenReturn(loginId);
        Mockito.when(patientRepository.getId(appointment.getPatient().getPID())).thenReturn(patientId);
        Mockito.when(doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId())).thenReturn(doctorId);
        Mockito.when(mapper.map(appointment,Appointment.class)).thenReturn(appointment1);

        Mockito.when(pdFGeneratorService.dateHandler(Mockito.any(LocalDate.class))).thenReturn(true);


        InvalidDate invalidDate = assertThrows(InvalidDate.class,()->{
            appointmentService.addAppointment(appointment,request);
        });

        assertThat(invalidDate).isNotNull();
        assertEquals(appointment.getAppointmentTime().toString()+":Invalid time",invalidDate.getMessage());

    }


    @Test
    void addAppointment_throwErrorIfForTheGivenTimeAppointmentAlreadyBooked() throws MessagingException, JSONException, UnsupportedEncodingException {

        Long loginId = 1L;
        Long patientId = 1L;
        Long doctorId = 3L;

        Map<String,String> expected = new HashMap<>();
        expected.put("appointId","1");
        expected.put("message",Constants.APPOINTMENT_CREATED);

        LocalDate localDate = LocalDate.of(2022,07,12);
        LocalTime localTime = LocalTime.of(10,30);



        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(12L);

        Appointment appointment = new Appointment(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmail.com",
                "pranay", localTime,true,"completed",null,null,null,true,2L,patient,doctorDetails,null,null);

        AppointmentDto appointment1 = new AppointmentDto(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",null,2L,patient,doctorDetails);



        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(loginId);
        Mockito.when(loginRepo.isIdAvailable(loginId)).thenReturn(loginId);
        Mockito.when(patientRepository.getId(appointment1.getPatient().getPID())).thenReturn(patientId);
        Mockito.when(doctorRepository.isIdAvailable(appointment1.getDoctorDetails().getId())).thenReturn(doctorId);
        Mockito.when(mapper.map(appointment1,Appointment.class)).thenReturn(appointment);
        Mockito.doReturn(appointment).when(appointmentRepository).save(Mockito.any(Appointment.class));


        //BookAgainHandler
        Mockito.when(appointmentRepository.existsById(appointment1.getFollowUpAppointmentId())).thenReturn(true);
        Mockito.when(appointmentRepository.getAppointmentById(Mockito.any(Long.class))).thenReturn(appointment);
        Mockito.when(pdFGeneratorService.dateHandler(Mockito.any(LocalDate.class))).thenReturn(true);


        Mockito.doNothing().when(mailService).mailServiceHandler(anyString(),anyString(),anyString(),anyString(),anyString());
        Mockito.doNothing().when(mailSender).send((MimeMessage) any());
        Mockito.doReturn(mimeMessage).when(mailSender).createMimeMessage();
        Mockito.when(loginRepo.email(appointment1.getDoctorDetails().getId())).thenReturn("pranay23@gmail.com");
        Mockito.when(pdFGeneratorService.formatDate(appointment1.getDateOfAppointment().toString())).thenReturn("12-07-2022");

        ResponseEntity<GenericMessage> response = appointmentService.addAppointment(appointment1,request);
        System.out.println(response);

        InvalidDate invalidDate = assertThrows(InvalidDate.class,()->{
            appointmentService.addAppointment(appointment1,request);
        });

        assertThat(invalidDate).isNotNull();
        assertEquals(appointment1.getAppointmentTime().toString()+":"+Constants.APPOINTMENT_ALREADY_BOOKED,invalidDate.getMessage());

    }



    @Test
    void addAppointment_IsBookedAgain() throws MessagingException, JSONException, UnsupportedEncodingException {

        Long loginId = 1L;
        Long patientId = 1L;
        Long doctorId = 12L;
        Long followUp = 2L;

        Map<String,String> expected = new HashMap<>();
        expected.put("appointId","1");
        expected.put("message",Constants.APPOINTMENT_CREATED);

        LocalDate localDate = LocalDate.of(2022,07,11);
        LocalTime localTime = LocalTime.of(12,00);



        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(12L);

        Appointment appointment1 = new Appointment(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmail.com",
                "pranay", localTime,true,"completed",null,null,null,true,2L,patient,doctorDetails,null,null);

        AppointmentDto appointment = new AppointmentDto(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",true,2L,patient,doctorDetails);



        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(loginId);
        Mockito.when(loginRepo.isIdAvailable(loginId)).thenReturn(loginId);
        Mockito.when(patientRepository.getId(appointment.getPatient().getPID())).thenReturn(patientId);
        Mockito.when(doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId())).thenReturn(doctorId);
        Mockito.when(mapper.map(appointment,Appointment.class)).thenReturn(appointment1);
        Mockito.doReturn(appointment1).when(appointmentRepository).save(Mockito.any(Appointment.class));


        //BookAgainHandler
        Mockito.when(appointmentRepository.existsById(appointment.getFollowUpAppointmentId())).thenReturn(true);
        Mockito.when(appointmentRepository.getAppointmentById(Mockito.any(Long.class))).thenReturn(appointment1);
        Mockito.when(pdFGeneratorService.dateHandler(Mockito.any(LocalDate.class))).thenReturn(true);

        Mockito.doNothing().when(mailService).mailServiceHandler(anyString(),anyString(),anyString(),anyString(),anyString());
        Mockito.doNothing().when(mailSender).send((MimeMessage) any());
        Mockito.doReturn(mimeMessage).when(mailSender).createMimeMessage();
        Mockito.when(loginRepo.email(appointment.getDoctorDetails().getId())).thenReturn("pranay23@gmail.com");
        Mockito.when(pdFGeneratorService.formatDate(appointment.getDateOfAppointment().toString())).thenReturn("12-07-2022");

        ResponseEntity<GenericMessage> response = appointmentService.addAppointment(appointment,request);
        System.out.println(response);

        assertThat(response).isNotNull();
        assertEquals(expected,response.getBody().getData());

    }


    @Test
    void addAppointment_ThrowErrorIfFollowUpIdNotPresentInDetailsProvided() throws MessagingException, JSONException, UnsupportedEncodingException {

        Long loginId = 1L;
        Long patientId = 1L;
        Long doctorId = 12L;

        Map<String,String> expected = new HashMap<>();
        expected.put("appointId","1");
        expected.put("message",Constants.APPOINTMENT_CREATED);

        LocalDate localDate = LocalDate.of(2022,07,12);
        LocalTime localTime = LocalTime.of(10,30);



        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(12L);

        Appointment appointment1 = new Appointment(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmail.com",
                "pranay", localTime,true,"completed",null,null,null,true,null,patient,doctorDetails,null,null);

        AppointmentDto appointment = new AppointmentDto(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",true,null,patient,doctorDetails);



        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(loginId);
        Mockito.when(loginRepo.isIdAvailable(loginId)).thenReturn(loginId);
        Mockito.when(patientRepository.getId(appointment.getPatient().getPID())).thenReturn(patientId);
        Mockito.when(doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId())).thenReturn(doctorId);
        Mockito.when(mapper.map(appointment,Appointment.class)).thenReturn(appointment1);
        Mockito.doReturn(appointment1).when(appointmentRepository).save(Mockito.any(Appointment.class));

        //BookAgainHandler
        Mockito.when(appointmentRepository.existsById(appointment.getFollowUpAppointmentId())).thenReturn(true);
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,()->{
            appointmentService.addAppointment(appointment,request);
        });

        assertThat(resourceNotFoundException).isNotNull();
        assertEquals(Constants.APPOINTMENT_NOT_FOUND,resourceNotFoundException.getMessage());

    }

    @Test
    void addAppointment_ThrowErrorIfFollowUpIdNotFoundAppointmentDb() throws MessagingException, JSONException, UnsupportedEncodingException {

        Long loginId = 1L;
        Long patientId = 1L;
        Long doctorId = 12L;

        LocalDate localDate = LocalDate.of(2022,07,12);
        LocalTime localTime = LocalTime.of(10,30);

        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(12L);

        Appointment appointment1 = new Appointment(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmail.com",
                "pranay", localTime,true,"completed",null,null,null,true,2L,patient,doctorDetails,null,null);

        AppointmentDto appointment = new AppointmentDto(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",true,null,patient,doctorDetails);




        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(loginId);
        Mockito.when(loginRepo.isIdAvailable(loginId)).thenReturn(loginId);
        Mockito.when(patientRepository.getId(appointment.getPatient().getPID())).thenReturn(patientId);
        Mockito.when(doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId())).thenReturn(doctorId);
        Mockito.when(mapper.map(appointment,Appointment.class)).thenReturn(appointment1);


        //BookAgainHandler
        Mockito.when(appointmentRepository.existsById(appointment.getFollowUpAppointmentId())).thenReturn(false);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,()->{
            appointmentService.addAppointment(appointment,request);
        });

        assertThat(resourceNotFoundException).isNotNull();
        assertEquals(Constants.APPOINTMENT_NOT_FOUND,resourceNotFoundException.getMessage());
    }

    @Test
    void addAppointment_ThrowErrorNullValueOfFollowUp() throws MessagingException, JSONException, UnsupportedEncodingException {

        Long loginId = 1L;
        Long patientId = 1L;
        Long doctorId = 12L;

        LocalDate localDate = LocalDate.of(2022,07,12);
        LocalTime localTime = LocalTime.of(10,30);

        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(12L);

        Appointment appointment1 = new Appointment(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmail.com",
                "pranay", localTime,true,"completed",null,null,null,true,null,patient,doctorDetails,null,null);

        AppointmentDto appointment = new AppointmentDto(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",true,null,patient,doctorDetails);



        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(loginId);
        Mockito.when(loginRepo.isIdAvailable(loginId)).thenReturn(loginId);
        Mockito.when(patientRepository.getId(appointment.getPatient().getPID())).thenReturn(patientId);
        Mockito.when(doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId())).thenReturn(doctorId);
        Mockito.when(mapper.map(appointment,Appointment.class)).thenReturn(appointment1);


        //BookAgainHandler
        Mockito.when(appointmentRepository.existsById(appointment.getFollowUpAppointmentId())).thenReturn(false);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,()->{
            appointmentService.addAppointment(appointment,request);
        });

        assertThat(resourceNotFoundException).isNotNull();
        assertEquals(Constants.APPOINTMENT_NOT_FOUND,resourceNotFoundException.getMessage());
    }


    @Test
    void addAppointment_throwErrorForPatientIdDoesNotMatchesWithAppointmentDetailProvided() throws MessagingException, JSONException, UnsupportedEncodingException {

        Long loginId = 1L;
        Long patientId = 1L;
        Long doctorId = 12L;

        LocalDate localDate = LocalDate.of(2022,07,12);
        LocalTime localTime = LocalTime.of(10,30);

        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        Patient patient1 = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(2L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(12L);



        AppointmentDto appointment1 = new AppointmentDto(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmal.com",
                "pranay", localTime,true,"completed",true,2L,patient,doctorDetails);

        Appointment appointment = new Appointment(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmail.com",
                "pranay", localTime,true,"completed",null,null,null,true,2L,patient,doctorDetails,null,null);

        Appointment appointment2 = new Appointment(1L,"dentist", localDate,"fever","sagar","sagarssn23@gmail.com",
                "pranay", localTime,true,"completed",null,null,null,true,2L,patient1,doctorDetails,null,null);


        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(loginId);
        Mockito.when(loginRepo.isIdAvailable(loginId)).thenReturn(loginId);
        Mockito.when(patientRepository.getId(appointment1.getPatient().getPID())).thenReturn(patientId);
        Mockito.when(doctorRepository.isIdAvailable(appointment1.getDoctorDetails().getId())).thenReturn(doctorId);
        Mockito.when(mapper.map(appointment1,Appointment.class)).thenReturn(appointment);


        //BookAgainHandler
        Mockito.when(appointmentRepository.existsById(appointment1.getFollowUpAppointmentId())).thenReturn(true);
        Mockito.when(appointmentRepository.getAppointmentById(Mockito.any(Long.class))).thenReturn(appointment2);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,()->{
            appointmentService.addAppointment(appointment1,request);
        });

        assertThat(resourceNotFoundException).isNotNull();
        assertEquals(Constants.APPOINTMENT_NOT_FOUND,resourceNotFoundException.getMessage());
    }


    @Test
    void checkSanityOfAppointment_SUCCESS(){

        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(2L);



        LoginDetails loginDetails1 = new LoginDetails(1L,"sagar","sagarssn23@gmail.com","google","profilePic","PATIENT",null,null,null);
        LoginDetails loginDetails2 = new LoginDetails(1L,"pranay","sagarssn23@gmail.com","google","profilePic","PATIENT",null,null,null);

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmail.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,patient,doctorDetails,null,null);

        Mockito.when(loginRepo.findById(patient.getPID())).thenReturn(Optional.of(loginDetails1));
        Mockito.when(loginRepo.findById(doctorDetails.getId())).thenReturn(Optional.of(loginDetails2));

        appointmentService.checkSanityOfAppointment(appointment);
    }

    @Test
    void checkSanityOfAppointment_PatientDetailEmpty(){

        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(2L);



        LoginDetails loginDetails1 = new LoginDetails(1L,"sagar","sagarssn23@gmail.com","google","profilePic","PATIENT",null,null,null);
        LoginDetails loginDetails2 = new LoginDetails(1L,"pranay","sagarssn23@gmail.com","google","profilePic","PATIENT",null,null,null);

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmail.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,patient,doctorDetails,null,null);

        Mockito.when(loginRepo.findById(patient.getPID())).thenReturn(Optional.empty());
        Mockito.when(loginRepo.findById(doctorDetails.getId())).thenReturn(Optional.of(loginDetails2));

        appointmentService.checkSanityOfAppointment(appointment);
    }

    @Test
    void checkSanityOfAppointment_DoctorDetailEmpty(){

        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(2L);



        LoginDetails loginDetails1 = new LoginDetails(1L,"sagar","sagarssn23@gmail.com","google","profilePic","PATIENT",null,null,null);
        LoginDetails loginDetails2 = new LoginDetails(1L,"pranay","sagarssn23@gmail.com","google","profilePic","PATIENT",null,null,null);

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmail.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,patient,doctorDetails,null,null);

        Mockito.when(loginRepo.findById(patient.getPID())).thenReturn(Optional.of(loginDetails1));
        Mockito.when(loginRepo.findById(doctorDetails.getId())).thenReturn(Optional.empty());

        appointmentService.checkSanityOfAppointment(appointment);
    }

    @Test
    void checkSanityOfAppointment_InvalidPatientEmail(){
        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(1L);



        LoginDetails loginDetails1 = new LoginDetails(1L,"sagar","sagarssn23@gmail.com","google","profilePic","PATIENT",null,null,null);
        LoginDetails loginDetails2 = new LoginDetails(2L,"pranay","pranay@gmail.com","nineleaps","profilePic1","DOCTOR",null,null,null);

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,patient,doctorDetails,null,null);

        Mockito.when(loginRepo.findById(patient.getPID())).thenReturn(Optional.of(loginDetails1));
        Mockito.when(loginRepo.findById(doctorDetails.getId())).thenReturn(Optional.of(loginDetails2));

        ValidationsException exception = assertThrows(ValidationsException.class,()->{
            appointmentService.checkSanityOfAppointment(appointment);
        });

        assertEquals(Constants.INVALID_PATIENT_EMAIL,exception.getMessages().get(0));
    }

    @Test
    void checkSanityOfAppointment_InvalidPatientName(){
        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(1L);



        LoginDetails loginDetails1 = new LoginDetails(1L,"sagar","sagarssn@gmail.com","google","profilePic","PATIENT",null,null,null);

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagarrr","sagarssn@gmail.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,patient,doctorDetails,null,null);

        Mockito.when(loginRepo.findById(patient.getPID())).thenReturn(Optional.of(loginDetails1));

        ValidationsException exception = assertThrows(ValidationsException.class,()->{
            appointmentService.checkSanityOfAppointment(appointment);
        });

        assertEquals(Constants.INVALID_PATIENT_NAME,exception.getMessages().get(0));
    }

    @Test
    void checkSanityOfAppointment_InvalidDoctorName(){

        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(2L);



        LoginDetails loginDetails1 = new LoginDetails(1L,"sagar","sagarssn23@gmail.com","google","profilePic","PATIENT",null,null,null);
        LoginDetails loginDetails2 = new LoginDetails(1L,"pranayyy","sagarssn23@gmail.com","google","profilePic","PATIENT",null,null,null);

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmail.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,patient,doctorDetails,null,null);

        Mockito.when(loginRepo.findById(patient.getPID())).thenReturn(Optional.of(loginDetails1));
        Mockito.when(loginRepo.findById(doctorDetails.getId())).thenReturn(Optional.of(loginDetails2));

        ValidationsException exception = assertThrows(ValidationsException.class,()->{
            appointmentService.checkSanityOfAppointment(appointment);
        });

        assertEquals(Constants.INVALID_DOCTOR_NAME,exception.getMessages().get(0));
    }



    @Test
    void getAllAppointmentByPatientId_SUCCESS() {
        final Long patientId = 1L;
        int pageNo = 2;

        Pageable paging= PageRequest.of(pageNo, 10);

        Map<String, List<PatientAppointmentListDto>> getAllAppointment =new HashMap<>();
        PatientAppointmentListDto dto1 = new PatientAppointmentListDto(2L,"dentist", LocalDate.now(),LocalTime.now(),"sagar","completed",true);
        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,null,null,null,null);

        List<Appointment> list = new ArrayList<>(Arrays.asList(appointment,appointment));
        Page<Appointment> list1=new PageImpl<>(list);

        List<PatientAppointmentListDto> dto = new ArrayList<>(Arrays.asList(dto1,dto1));
        getAllAppointment.put("past",dto);
        getAllAppointment.put("today",new ArrayList<>(Arrays.asList(dto1,dto1,dto1,dto1)));
        getAllAppointment.put("upcoming",dto);

        Mockito.when(patientRepository.getId(Mockito.any(Long.class))).thenReturn(patientId);
        Mockito.when(appointmentRepository.pastAppointment(patientId,paging)).thenReturn(list1);
        Mockito.when(appointmentRepository.todayAppointment1(patientId,paging)).thenReturn(list1);
        Mockito.when(appointmentRepository.todayAppointment2(patientId,paging)).thenReturn(list1);
        Mockito.when(appointmentRepository.upcomingAppointment(patientId,paging)).thenReturn(list1);
        Mockito.when(mapper.map(appointment,PatientAppointmentListDto.class)).thenReturn(dto1);

        ResponseEntity<GenericMessage> newAppointmentList = appointmentService.getAllAppointmentByPatientId(patientId,pageNo);
        assertThat(newAppointmentList).isNotNull();
        assertEquals(getAllAppointment,newAppointmentList.getBody().getData());
    }

    @Test
    void throwErrorIfIdNotFoundInPatientDbForAllAppointmentPatient(){
        final Long patientId = 1L;
        int pageNo = 2;
        Mockito.when(patientRepository.getId(patientId)).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            appointmentService.getAllAppointmentByPatientId(patientId,pageNo);
        });
        assertEquals(Constants.PATIENT_NOT_FOUND,resourceNotFoundException.getMessage());

    }

    @Test
    void getAllAppointmentByDoctorId_SUCCESS() {

        final Long doctorId = 1L;
        int pageNo = 2;

        Pageable paging= PageRequest.of(pageNo, 10);

        Map<String, List<DoctorAppointmentListDto>> getAllAppointment =new HashMap<>();
        DoctorAppointmentListDto dto1 = new DoctorAppointmentListDto(2L, LocalDate.now(),"sagar","sagarssn23@gmal.com","completed",LocalTime.now());
        List<DoctorAppointmentListDto> dto = new ArrayList<>(Arrays.asList(dto1,dto1));

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,null,null,null,null);

        List<Appointment> list = new ArrayList<>(Arrays.asList(appointment,appointment));
        Page<Appointment> list1=new PageImpl<>(list);


        getAllAppointment.put("past",dto);
        getAllAppointment.put("today",new ArrayList<>(Arrays.asList(dto1,dto1,dto1,dto1)));
        getAllAppointment.put("upcoming",dto);

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(doctorId);
        Mockito.when(appointmentRepository.pastDoctorAppointment(doctorId,paging)).thenReturn(list1);
        Mockito.when(appointmentRepository.todayDoctorAppointment1(doctorId,paging)).thenReturn(list1);
        Mockito.when(appointmentRepository.todayDoctorAppointment2(doctorId,paging)).thenReturn(list1);
        Mockito.when(appointmentRepository.upcomingDoctorAppointment(doctorId,paging)).thenReturn(list1);
        Mockito.when(mapper.map(appointment,DoctorAppointmentListDto.class)).thenReturn(dto1);

        ResponseEntity<GenericMessage> newAppointmentList = appointmentService.getAllAppointmentByDoctorId(doctorId,pageNo);
        assertThat(newAppointmentList).isNotNull();
        assertEquals(getAllAppointment,newAppointmentList.getBody().getData());

    }

    @Test
    void throwErrorIfIdNotFoundInDoctorDbForAllAppointmentDoctor(){
        final Long doctorId = 1L;
        int pageNo = 2;

        Mockito.when(doctorRepository.isIdAvailable(doctorId)).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            appointmentService.getAllAppointmentByDoctorId(doctorId,pageNo);
        });
        assertEquals(Constants.DOCTOR_NOT_FOUND,resourceNotFoundException.getMessage());

    }


    @Test
    void weeklyDoctorCountChart_SUCCESS() {
        final Long doctorId = 1L;
        ArrayList<String> newList = new ArrayList<>(Arrays.asList("1-7,1","8-14,1","15-21,1","22-28,1","29-31,3"));


        LocalDate localDate = LocalDate.of(2022,07,04);
        LocalDate localDate1 = LocalDate.of(2022,07,10);
        LocalDate localDate2 = LocalDate.of(2022,07,16);
        LocalDate localDate3 = LocalDate.of(2022,07,28);
        LocalDate localDate4 = LocalDate.of(2022,07,29);
        LocalDate localDate5 = LocalDate.of(2022,07,30);
        LocalDate localDate6 = LocalDate.of(2022,07,31);



        Date date = Date.valueOf(localDate);
        Date date1 = Date.valueOf(localDate1);
        Date date2 = Date.valueOf(localDate2);
        Date date3 = Date.valueOf(localDate3);
        Date date4 = Date.valueOf(localDate4);
        Date date5 = Date.valueOf(localDate5);
        Date date6 = Date.valueOf(localDate6);

        ArrayList<java.sql.Date> dateList = new ArrayList<>(Arrays.asList(date,date1,date2,date3,date4,date5,date6));
        System.out.println(dateList);

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(doctorId);
        Mockito.when(appointmentRepository.getAllDatesByDoctorId(doctorId)).thenReturn(dateList);
        Mockito.when(pdFGeneratorService.monthHandler(Mockito.any(String.class))).thenReturn("07");

        ResponseEntity<GenericMessage> newChart = appointmentService.weeklyDoctorCountChart(doctorId);
        System.out.println(newChart.getBody().getData());
        assertThat(newChart).isNotNull();
        assertEquals(newList,newChart.getBody().getData());

    }

    @Test
    void weeklyDoctorCountChart_ThrowErrorIfInvalidDateProvided() {
        final Long doctorId = 1L;
        ArrayList<String> newList = new ArrayList<>(Arrays.asList("1-7,1","8-14,1","15-21,1","22-28,1","29-31,3"));


        LocalDate localDate = LocalDate.of(2023,07,04);
        LocalDate localDate1 = LocalDate.of(2022,07,10);
        LocalDate localDate2 = LocalDate.of(2022,07,16);
        LocalDate localDate3 = LocalDate.of(2022,07,28);
        LocalDate localDate4 = LocalDate.of(2022,07,29);
        LocalDate localDate5 = LocalDate.of(2022,07,30);
        LocalDate localDate6 = LocalDate.of(2022,07,31);



        Date date = Date.valueOf(localDate);
        Date date1 = Date.valueOf(localDate1);
        Date date2 = Date.valueOf(localDate2);
        Date date3 = Date.valueOf(localDate3);
        Date date4 = Date.valueOf(localDate4);
        Date date5 = Date.valueOf(localDate5);
        Date date6 = Date.valueOf(localDate6);

        ArrayList<java.sql.Date> dateList = new ArrayList<>(Arrays.asList(date,date1,date2,date3,date4,date5,date6));
        System.out.println(dateList);

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(doctorId);
        Mockito.when(appointmentRepository.getAllDatesByDoctorId(doctorId)).thenReturn(dateList);
        Mockito.when(pdFGeneratorService.monthHandler(Mockito.any(String.class))).thenReturn("07");

        InvalidDate invalidDate = assertThrows(InvalidDate.class,()->{
            appointmentService.weeklyDoctorCountChart(doctorId);
        });
        assertThat(invalidDate).isNotNull();
        assertEquals("Invalid date",invalidDate.getMessage());
    }



    @Test
    void throwErrorIfIdNotFoundInDoctorDbForWeeklyCountChartDoctor(){
        final Long doctorId = 1L;

        Mockito.when(doctorRepository.isIdAvailable(doctorId)).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            appointmentService.weeklyDoctorCountChart(doctorId);
        });
        assertEquals(Constants.DOCTOR_NOT_FOUND,resourceNotFoundException.getMessage());

    }

    @Test
    void weeklyPatientCountChart_SUCCESS() {
        final Long patientId = 1L;
        ArrayList<String> newList = new ArrayList<>(Arrays.asList("1-7,1","8-14,1","15-21,1","22-28,1","29-31,1"));


        LocalDate localDate = LocalDate.of(2022,07,01);
        LocalDate localDate1 = LocalDate.of(2022,07,11);
        LocalDate localDate2 = LocalDate.of(2022,07,17);
        LocalDate localDate3 = LocalDate.of(2022,07,26);
        LocalDate localDate4 = LocalDate.of(2022,07,30);



        Date date = Date.valueOf(localDate);
        Date date1 = Date.valueOf(localDate1);
        Date date2 = Date.valueOf(localDate2);
        Date date3 = Date.valueOf(localDate3);
        Date date4 = Date.valueOf(localDate4);

        ArrayList<java.sql.Date> dateList = new ArrayList<>(Arrays.asList(date,date1,date2,date3,date4));
        System.out.println(dateList);

        Mockito.when(patientRepository.getId(Mockito.any(Long.class))).thenReturn(patientId);
        Mockito.when(appointmentRepository.getAllDatesByPatientId(patientId)).thenReturn(dateList);
        Mockito.when(pdFGeneratorService.monthHandler(Mockito.any(String.class))).thenReturn("07");

        ResponseEntity<GenericMessage> newChart = appointmentService.weeklyPatientCountChart(patientId);
        assertThat(newChart).isNotNull();
        assertEquals(newList, Objects.requireNonNull(newChart.getBody()).getData());
    }

    @Test
    void throwErrorIfIdNotFoundInPatientDbForWeeklyCountChartPatient(){
        final Long patientId = 1L;

        Mockito.when(patientRepository.getId(patientId)).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            appointmentService.weeklyPatientCountChart(patientId);
        });
        assertEquals(Constants.PATIENT_NOT_FOUND,resourceNotFoundException.getMessage());

    }




    @Test
    void getFollowDetails_SUCCESS() {
        final Long appointId = 1L;
        final Long appointIdDb = 1L;
        FollowUpDto followUpDto = new FollowUpDto(1L,"sagar",1L,"dentist","completed");

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,null,null,null,null);

        Mockito.when(appointmentRepository.getId(appointId)).thenReturn(appointIdDb);
        Mockito.when(appointmentRepository.getFollowUpData(appointId)).thenReturn(appointment);
        Mockito.when(mapper.map(appointment,FollowUpDto.class)).thenReturn(followUpDto);

        ResponseEntity<GenericMessage> newFollowUpDetails = appointmentService.getFollowDetails(appointId);
        assertThat(newFollowUpDetails).isNotNull();
        assertEquals(followUpDto, Objects.requireNonNull(newFollowUpDetails.getBody()).getData());

    }

    @Test
    void throwErrorIfAppointmentIdNotFoundInAppointmentDbForFollowDetails() {
        final  Long appointId = 1L;
        final Long appointIdDb = 2L;

        Mockito.when(appointmentRepository.getId(appointId)).thenReturn(appointIdDb);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            appointmentService.getFollowDetails(appointId);
        });
        assertEquals(Constants.APPOINTMENT_NOT_FOUND,resourceNotFoundException.getMessage());

    }

    @Test
    void throwErrorIfAppointmentIdNotEqualToIdProvidedForFollowDetails() {
        final  Long appointId = 1L;

        Mockito.when(appointmentRepository.getId(appointId)).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            appointmentService.getFollowDetails(appointId);
        });
        assertEquals(Constants.APPOINTMENT_NOT_FOUND,resourceNotFoundException.getMessage());

    }

    @Test
    void getAppointmentById_SUCCESS() {
        final Long appointId = 1L;
        final Long appointIdDb = 1L;

        PatientProfileDto patientProfileDto = new PatientProfileDto(1L,LocalDate.now(),"sagar","sagarssn3@gmail.com",
                "fever","dentist",true,1L,null,null,null,"completed");

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,null,null,null,null);

        Mockito.when(appointmentRepository.getId(appointId)).thenReturn(appointIdDb);
        Mockito.when(appointmentRepository.getAppointmentById(appointId)).thenReturn(appointment);
        Mockito.when(mapper.map(appointment, PatientProfileDto.class)).thenReturn(patientProfileDto);

        ResponseEntity<GenericMessage> newPatientProfileDetails = appointmentService.getAppointmentById(appointId);
        assertThat(newPatientProfileDetails).isNotNull();
        assertEquals(patientProfileDto, Objects.requireNonNull(newPatientProfileDetails.getBody()).getData());
    }

    @Test
    void throwErrorIfAppointmentIdNotFoundInAppointmentDbForAppointmentById() {
        final Long appointId = 1L;

        Mockito.when(appointmentRepository.getId(appointId)).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            appointmentService.getAppointmentById(appointId);
        });
        assertEquals(Constants.APPOINTMENT_NOT_FOUND,resourceNotFoundException.getMessage());

    }



    @Test
    void recentAppointment_SUCCESS() {
        Long id = 1L;
        DoctorAppointmentListDto dto1 = new DoctorAppointmentListDto(2L, LocalDate.now(),"sagar","sagarssn23@gmal.com","completed", LocalTime.now());
        List<DoctorAppointmentListDto> dto = new ArrayList<>(Arrays.asList(dto1,dto1));

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,null,null,null,null);
       List<Appointment> list = new ArrayList<>(Arrays.asList(appointment,appointment));

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(id);
        Mockito.when(appointmentRepository.recentAppointment(id)).thenReturn(list);
        Mockito.when(mapper.map(appointment,DoctorAppointmentListDto.class)).thenReturn(dto1);

        ResponseEntity<GenericMessage> newList = appointmentService.recentAppointment(id);
        assertThat(newList).isNotNull();
        assertEquals(dto,newList.getBody().getData());
    }

    @Test
    void throwErrorIfDoctorIdNotFoundForRecentAppointment() {
        Long id = 1L;
        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            appointmentService.recentAppointment(id);
        });
        assertEquals(Constants.DOCTOR_NOT_FOUND,resourceNotFoundException.getMessage());

    }


    @Test
    void totalNoOfAppointment_SUCCESS() {
        Long doctorId = 1L;
        int count = 10;

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(doctorId);
        Mockito.when(appointmentRepository.totalNoOfAppointment(doctorId)).thenReturn(count);
        ResponseEntity<GenericMessage> newCount = appointmentService.totalNoOfAppointment(doctorId);
        assertThat(newCount).isNotNull();
        assertEquals(count,newCount.getBody().getData());

    }

    @Test
    void throwErrorIfDoctorIdNotFoundForTotalNoOfAppointment() {
        Long doctorId = 1L;
        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            appointmentService.totalNoOfAppointment(doctorId);
        });
        assertEquals(Constants.DOCTOR_NOT_FOUND,resourceNotFoundException.getMessage());

    }

    @Test
    void todayAppointments_SUCCESS() {
        Long doctorId = 1L;
        int count = 5;

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(doctorId);
        Mockito.when(appointmentRepository.todayAppointments(doctorId)).thenReturn(count);
        ResponseEntity<GenericMessage> newCount = appointmentService.todayAppointments(doctorId);
        assertThat(newCount).isNotNull();
        assertEquals(count,newCount.getBody().getData());
    }

    @Test
    void throwErrorIfDoctorIdNotFoundForTodayAppointment() {
        Long doctorId = 1L;
        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            appointmentService.todayAppointments(doctorId);
        });
        assertEquals(Constants.DOCTOR_NOT_FOUND,resourceNotFoundException.getMessage());

    }

    @Test
    void totalNoOfAppointmentAddedThisWeek_SUCCESS() {
        Long doctorId = 1L;
        int count = 8;

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(doctorId);
        Mockito.when(appointmentRepository.totalNoOfAppointmentAddedThisWeek(doctorId)).thenReturn(count);
        ResponseEntity<GenericMessage> newCount = appointmentService.totalNoOfAppointmentAddedThisWeek(doctorId);
        assertThat(newCount).isNotNull();
        assertEquals(count,newCount.getBody().getData());
    }

    @Test
    void throwErrorIfDoctorIdNotFoundForAppointmentThisWeek() {
        Long doctorId = 1L;
        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            appointmentService.totalNoOfAppointmentAddedThisWeek(doctorId);
        });
        assertEquals(Constants.DOCTOR_NOT_FOUND,resourceNotFoundException.getMessage());

    }


    @Test
    void patientCategoryGraph_SUCCESS() {
        Long patientId = 1L;

        ArrayList<String> chartsPatient = new ArrayList<>();
        chartsPatient.add("week1,2");
        chartsPatient.add("week2,3");
        chartsPatient.add("week3,4");
        chartsPatient.add("week4,5");
        chartsPatient.add("week5,1");


        Mockito.when(patientRepository.getId(Mockito.any(Long.class))).thenReturn(patientId);
        Mockito.when(appointmentRepository.patientCategoryGraph(patientId)).thenReturn(chartsPatient);
        ResponseEntity<GenericMessage> newChart = appointmentService.patientCategoryGraph(patientId);
        assertThat(newChart).isNotNull();
        assertEquals(chartsPatient,newChart.getBody().getData());
    }


    @Test
    void throwErrorIfDoctorIdNotFoundForPatientCategoryGraph() {
        Long patientId = 1L;
        Mockito.when(patientRepository.getId(Mockito.any(Long.class))).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            appointmentService.patientCategoryGraph(patientId);
        });
        assertEquals(Constants.PATIENT_NOT_FOUND,resourceNotFoundException.getMessage());

    }




    @Test
    void checkSlots_SelectedDateIsUnderOneWeekRangeAndDoctorBookedSlotsIsNotEmpty(){
        LocalDate localDate = LocalDate.of(2022,07,07);
        LocalDate localDate1 = LocalDate.of(2022,07,11);

        Long doctorId = 1L;

        List<Boolean> timesSlots=List.of(true,false,true,false,true,false,true,true,true,true,true,true);
        Map<LocalDate,List<Boolean>> list = new HashMap<>();
        list.put(localDate,timesSlots);
        Map<Long,Map<LocalDate,List<Boolean>>> slots=new HashMap<>();
        slots.put(doctorId,list);

        LocalTime localTime = LocalTime.of(10,30);
        LocalTime localTime1 = LocalTime.of(11,30);
        LocalTime localTime2 = LocalTime.of(12,30);


        Time time = Time.valueOf(localTime);
        Time time1 = Time.valueOf(localTime1);
        Time time2 = Time.valueOf(localTime2);

        List<Time> times = new ArrayList<>(Arrays.asList(time,time1,time2));

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(doctorId);
        Mockito.when(appointmentRepository.getTimesByIdAndDate(Mockito.any(LocalDate.class),Mockito.any(Long.class))).thenReturn(times);
        Mockito.when(pdFGeneratorService.dateHandler(Mockito.any(LocalDate.class))).thenReturn(true);

        List<Boolean> response = appointmentService.checkSlots(localDate,doctorId);
        System.out.println("round 2");
        List<Boolean> response1 = appointmentService.checkSlots(localDate1,doctorId);
        List<Boolean> response2 = appointmentService.checkSlots(localDate1,doctorId);

        assertAll(
                ()-> assertThat(response).isNotNull(),
                ()-> assertThat(response1).isNotNull(),
                ()-> assertThat(response2).isNotNull()


        );

        assertAll(
                ()-> assertEquals(timesSlots,response),
                ()-> assertEquals(timesSlots,response1),
                ()-> assertEquals(timesSlots,response2)
        );
    }



    @Test
    void checkSlots_throwErrorIfDoctorNotPresentWithIdProvided(){
        LocalDate localDate = LocalDate.of(2022,07,05);
        Long doctorId = 1L;

        List<Boolean> timesSlots=List.of(true,true,true,true,true,true,true,true,true,true,true,true);
        Map<LocalDate,List<Boolean>> list = new HashMap<>();
        list.put(localDate,timesSlots);
        Map<Long,Map<LocalDate,List<Boolean>>> slots=new HashMap<>();
        slots.put(doctorId,list);

        System.out.println(slots.get(doctorId));

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,()->{
            appointmentService.checkSlots(localDate,doctorId);
        }) ;

        assertThat(resourceNotFoundException).isNotNull();
        assertEquals(Constants.DOCTOR_NOT_FOUND,resourceNotFoundException.getMessage());
    }



    @Test
    void checkAvailableSlots_DoctorBookedSlotsIsEmpty(){
        Long doctorId = 5L;
        LocalDate localDate = LocalDate.of(2022,07,10);
        LocalDate localDate1 = LocalDate.of(2022,07,11);



        List<Boolean> timesSlots=List.of(true,true,true,true,true,true,true,true,true,true,true,true);
        Map<LocalDate,List<Boolean>> list = new HashMap<>();
        Map<Long,Map<LocalDate,List<Boolean>>> slots=new HashMap<>();
        slots.put(doctorId,list);
        System.out.println(slots);

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(doctorId);
        Mockito.when(appointmentRepository.getTimesByIdAndDate(Mockito.any(LocalDate.class),Mockito.any(Long.class))).thenReturn(new ArrayList<>());
        Mockito.when(pdFGeneratorService.dateHandler(Mockito.any(LocalDate.class))).thenReturn(true);

        List<Boolean> response = appointmentService.checkSlots(localDate,doctorId);
        System.out.println("2nd round");
        List<Boolean> response1 = appointmentService.checkSlots(localDate1,doctorId);
        System.out.println("3rd round");

        System.out.println(response);
        System.out.println(response1);


        assertAll(
                ()-> assertThat(response).isNotNull(),
                ()-> assertThat(response1).isNotNull()

        );

        assertAll(
                ()-> assertEquals(timesSlots,response),
                ()-> assertEquals(timesSlots,response1)
        );

    }




    @Test
    void checkSlots_ThrowErrorIfDateIsNotValid(){
        LocalDate localDate = LocalDate.of(2022,07,07);
        LocalDate localDate1 = LocalDate.of(2022,07,16);
        LocalDate localDate2 = LocalDate.of(2022,07,04);


        Long doctorId = 9L;

        List<Boolean> timesSlots=List.of(true,true,true,true,true,true,true,true,true,true,true,true);
        Map<LocalDate,List<Boolean>> list = new HashMap<>();
        Map<Long,Map<LocalDate,List<Boolean>>> slots=new HashMap<>();
        slots.put(doctorId,list);
        System.out.println(slots);

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(doctorId);
        Mockito.when(appointmentRepository.getTimesByIdAndDate(Mockito.any(LocalDate.class),Mockito.any(Long.class))).thenReturn(new ArrayList<>());
        Mockito.when(pdFGeneratorService.dateHandler(localDate)).thenReturn(true);

        List<Boolean> response = appointmentService.checkSlots(localDate,doctorId);

        System.out.println(response);


        InvalidDate invalidDate = assertThrows(InvalidDate.class,()->{
            appointmentService.checkSlots(localDate1,doctorId);
        }) ;

        InvalidDate invalidDate1 = assertThrows(InvalidDate.class,()->{
            appointmentService.checkSlots(localDate2,doctorId);
        }) ;
        assertAll(
                ()-> assertThat(invalidDate).isNotNull(),
                ()-> assertThat(invalidDate1).isNotNull()

        );

        assertAll(
                ()-> assertEquals(localDate1.toString()+":"+Constants.SELECT_SPECIFIED_DATES,invalidDate.getMessage()),
                ()-> assertEquals(localDate2.toString()+":"+Constants.SELECT_SPECIFIED_DATES,invalidDate1.getMessage())
        );
    }


    @Test
    void checkSlots_ThrowErrorIfSelectedDateIsNotUnderOneWeekRange(){
        LocalDate localDate = LocalDate.of(2022,07,04);
        LocalDate localDate1 = LocalDate.of(2022,07,16);

        Long doctorId = 7L;

        List<Boolean> timesSlots=List.of(true,true,true,true,true,true,true,true,true,true,true,true);
        Map<LocalDate,List<Boolean>> list = new HashMap<>();
        list.put(localDate,timesSlots);
        Map<Long,Map<LocalDate,List<Boolean>>> slots=new HashMap<>();
        slots.put(doctorId,list);

        System.out.println(slots.get(doctorId));

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(doctorId);

        InvalidDate invalidDate = assertThrows(InvalidDate.class,()->{
            appointmentService.checkSlots(localDate,doctorId);
        }) ;

        InvalidDate invalidDate1 = assertThrows(InvalidDate.class,()->{
            appointmentService.checkSlots(localDate1,doctorId);
        }) ;


        assertAll(
                ()-> assertThat(invalidDate).isNotNull(),
                ()-> assertThat(invalidDate1).isNotNull()

        );

        assertAll(
                ()-> assertEquals(localDate.toString()+":"+Constants.SELECT_SPECIFIED_DATES,invalidDate.getMessage()),
                ()-> assertEquals(localDate1.toString()+":"+Constants.SELECT_SPECIFIED_DATES,invalidDate1.getMessage())
        );
    }
}