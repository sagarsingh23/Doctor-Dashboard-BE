package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.entities.model.Appointment;
import com.dashboard.doctor_dashboard.entities.model.Patient;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.repository.*;
import com.dashboard.doctor_dashboard.services.patient_service.PatientServiceImpl;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.utils.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private LoginRepo loginRepo;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private PatientServiceImpl patientService;


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
    void addPatientTest_Success() {
        Long id = 1L;
        PatientEntityDto patientEntityDto = new PatientEntityDto("9728330045","Male",21,"A+","Address1","9728330045");
        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        Mockito.when(loginRepo.isIdAvailable(Mockito.any(Long.class))).thenReturn(id);
        Mockito.when(patientRepository.getPatientByLoginId(id)).thenReturn(patient);
        Mockito.when(mapper.map(patient,PatientEntityDto.class)).thenReturn(patientEntityDto);

        ResponseEntity<GenericMessage> newPatient = patientService.addPatient(patientEntityDto,id);

        assertThat(newPatient).isNotNull();
        assertEquals(patientEntityDto,newPatient.getBody().getData());
    }

    @Test
    void throwErrorIfIdNotPresentInDatabaseForAddDoctor() {
        Long id = 1L;
        PatientEntityDto patientEntityDto = new PatientEntityDto("9728330045","Male",21,"A+","Address1","9728330045");

        Mockito.when(loginRepo.isIdAvailable(Mockito.any(Long.class))).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,() -> {
            patientService.addPatient(patientEntityDto,id);
        });
    }

    @Test
    void getPatientProfileDetailsTest() {
        Long loginId = 1L;

        Patient patient = new Patient();
        patient.setAge(21);
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setBloodGroup("A+");
        patient.setAlternateMobileNo("900011112");

        PatientEntityDto patientEntityDto = new PatientEntityDto("9728330045","Male",21,"A+","Address1","9728330045");

        Mockito.when(loginRepo.isIdAvailable(Mockito.any(Long.class))).thenReturn(loginId);
        Mockito.when(patientRepository.getPatientByLoginId(loginId)).thenReturn(patient);
        Mockito.when(mapper.map(patient,PatientEntityDto.class)).thenReturn(patientEntityDto);

        ResponseEntity<GenericMessage> profileView = patientService.getPatientDetailsById(loginId);
        assertThat(profileView).isNotNull();
        assertEquals(patientEntityDto,profileView.getBody().getData());
    }

    @Test
    void throwErrorIfIdNotPresentInDatabaseForPatientProfile() {
        Long id = 1L;

        Mockito.when(loginRepo.isIdAvailable(Mockito.any(Long.class))).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,() -> {
            patientService.getPatientDetailsById(id);
        });
    }

    @Test
    void updatePatientDetails_Success() {
        final Long id = 1L;
        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto(1L,"9728330045");

        Mockito.when(loginRepo.existsById(updateDto.getId())).thenReturn(true);
        Mockito.when(patientRepository.getId(updateDto.getId())).thenReturn(id);

        patientService.updatePatientDetails(id,updateDto);
        patientService.updatePatientDetails(id,updateDto);

        verify(patientRepository,times(2)).updateMobileNo(updateDto.getMobileNo(),updateDto.getId());


    }


    @Test
    void throwErrorIfIdNotPresentInLoginDetailsDBForUpdatePatient() {
        final Long id = 1L;
        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto(1L,"9728330045");

        Mockito.when(loginRepo.existsById(Mockito.any(Long.class))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,() -> {
            patientService.updatePatientDetails(id,updateDto);
        });
    }

    @Test
    void throwErrorIfIdNotPresentInPatientDetailsDBForUpdatePatient() {
        final Long id = 1L;
        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto(1L,"9728330045");

        Mockito.when(loginRepo.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.when(patientRepository.getId(updateDto.getId())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,() -> {
            patientService.updatePatientDetails(id,updateDto);
        });
    }

    @Test
    void throwErrorIfIdNotPresentInDBForUpdatePatient() {
        final Long id = 1L;
        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto(1L,"9728330045");

        Mockito.when(loginRepo.existsById(Mockito.any(Long.class))).thenReturn(false);
        Mockito.when(patientRepository.getId(updateDto.getId())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,() -> {
            patientService.updatePatientDetails(id,updateDto);
        });
    }

    @Test
    void deletePatientById() {
        Long id = 1L;

        patientService.deletePatientById(id);
        patientService.deletePatientById(id);

        verify(patientRepository,times(2)).deleteById(id);
    }


    @Test
    void getNotificationTest_SUCCESS(){
        Long patientId = 1L;
        NotificationDto dto1 = new NotificationDto(1L,"Sagar");
        NotificationDto dto2 = new NotificationDto(2L,"pranay");
        NotificationDto dto3 = new NotificationDto(3L,"Gokul");
        List<NotificationDto> list = new ArrayList<>(Arrays.asList(dto1,dto2,dto3));


        Mockito.when(loginRepo.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.when(patientRepository.getId(Mockito.any(Long.class))).thenReturn(patientId);
        Mockito.when(appointmentRepository.getNotifications(patientId)).thenReturn(list);

        ResponseEntity<GenericMessage> notification = patientService.getNotifications(patientId);
        assertThat(notification).isNotNull();
        assertEquals(list,notification.getBody().getData());
    }

    @Test
    void throwErrorIfIdNotPresentInLoginDetailsDBForNotification() {
        Long patientId = 1L;

        Mockito.when(loginRepo.existsById(Mockito.any(Long.class))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,() -> {
            patientService.getNotifications(patientId);
        });
    }

    @Test
    void throwErrorIfIdNotPresentInPatientDetailsDBForNotification() {
        Long patientId = 1L;

        Mockito.when(loginRepo.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.when(patientRepository.getId(Mockito.any(Long.class))).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,() -> {
            patientService.getNotifications(patientId);
        });
    }

    @Test
    void throwErrorIfIdNotPresentInDBForNotification() {
        Long patientId = 1L;
        NotificationDto dto1 = new NotificationDto(1L,"Sagar");
        NotificationDto dto2 = new NotificationDto(2L,"pranay");
        NotificationDto dto3 = new NotificationDto(3L,"Gokul");
        List<NotificationDto> list = new ArrayList<>(Arrays.asList(dto1,dto2,dto3));


        Mockito.when(loginRepo.existsById(Mockito.any(Long.class))).thenReturn(false);
        Mockito.when(patientRepository.getId(Mockito.any(Long.class))).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,() -> {
            patientService.getNotifications(patientId);
        });
    }

    @Test
    void viewAppointment_SUCCESS() {
        Long id = 1L;
        Long appointmentId = 1L;
        Long patientId = 1L;
        Long loginId = 1L;
        Long doctorId = 1L;

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,false,true,2L,null,null,null,null);

        AppointmentViewDto viewDto = new AppointmentViewDto("Sagar","genral", LocalDate.now(), LocalTime.now(),"completed","B+", (short) 21,"Male");

        Mockito.when(patientRepository.getId(patientId)).thenReturn(loginId);
        Mockito.when(appointmentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(appointment));
        Mockito.when(appointmentRepository.getDoctorId(appointmentId)).thenReturn(id);
        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(doctorId);

        Mockito.when(appointmentRepository.getBasicAppointmentDetails(appointmentId,patientId)).thenReturn(viewDto);

        ResponseEntity<GenericMessage> appointmentView = patientService.viewAppointment(appointmentId,patientId);
        System.out.println(appointmentView.getBody().getData());
        assertThat(appointmentView).isNotNull();
        assertEquals(viewDto.toString(),appointmentView.getBody().getData().toString());
    }


    @Test
    void throwErrorIfIdNotPresentInDoctorDbForViewAppointment() {
        Long id = 1L;
        Long appointmentId = 1L;
        Long patientId = 1L;
        Long loginId = 1L;

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,false,true,2L,null,null,null,null);

        AppointmentViewDto viewDto = new AppointmentViewDto("Sagar","genral", LocalDate.now(), LocalTime.now(),"completed","B+", (short) 21,"Male");

        Mockito.when(patientRepository.getId(patientId)).thenReturn(loginId);
        Mockito.when(appointmentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(appointment));
        Mockito.when(appointmentRepository.getDoctorId(appointmentId)).thenReturn(id);
        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            patientService.viewAppointment(appointmentId,patientId);
        });
        assertEquals(Constants.DOCTOR_NOT_FOUND,resourceNotFoundException.getMessage());
    }

    @Test
    void throwErrorIfIdNotPresentInAppointmentDbForViewAppointment() {
        Long appointmentId = 1L;
        Long patientId = 1L;
        Long loginId = 1L;

        AppointmentViewDto viewDto = new AppointmentViewDto("Sagar","genral", LocalDate.now(), LocalTime.now(),"completed","B+", (short) 21,"Male");

        Mockito.when(patientRepository.getId(patientId)).thenReturn(loginId);
        Mockito.when(appointmentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            patientService.viewAppointment(appointmentId,patientId);
        });
        assertEquals(Constants.APPOINTMENT_NOT_FOUND,resourceNotFoundException.getMessage());
    }

    @Test
    void throwErrorIfDoctorIdNotPresentInAppointmentDbForViewAppointment() {
        Long appointmentId = 1L;
        Long patientId = 1L;
        Long loginId = 1L;

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,false,true,2L,null,null,null,null);

        AppointmentViewDto viewDto = new AppointmentViewDto("Sagar","genral", LocalDate.now(), LocalTime.now(),"completed","B+", (short) 21,"Male");

        Mockito.when(patientRepository.getId(patientId)).thenReturn(loginId);
        Mockito.when(appointmentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(appointment));
        Mockito.when(appointmentRepository.getDoctorId(appointmentId)).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            patientService.viewAppointment(appointmentId,patientId);
        });
        assertEquals(Constants.APPOINTMENT_NOT_FOUND,resourceNotFoundException.getMessage());
    }

    @Test
    void throwErrorIfAppointmentIdNotPresentInAppointmentDbForViewAppointment() {
        Long appointmentId = 1L;
        Long patientId = 1L;
        Long loginId = 1L;

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,false,true,2L,null,null,null,null);

        AppointmentViewDto viewDto = new AppointmentViewDto("Sagar","genral", LocalDate.now(), LocalTime.now(),"completed","B+", (short) 21,"Male");

        Mockito.when(patientRepository.getId(patientId)).thenReturn(loginId);
        Mockito.when(appointmentRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        Mockito.when(appointmentRepository.getDoctorId(appointmentId)).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            patientService.viewAppointment(appointmentId,patientId);
        });
        assertEquals(Constants.APPOINTMENT_NOT_FOUND,resourceNotFoundException.getMessage());
    }

    @Test
    void throwErrorIfIdNotFoundInPatientDbForAppointmentView() {
        Long appointmentId = 1L;
        Long patientId = 1L;

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,false,true,2L,null,null,null,null);

        AppointmentViewDto viewDto = new AppointmentViewDto("Sagar","genral", LocalDate.now(), LocalTime.now(),"completed","B+", (short) 21,"Male");

        Mockito.when(patientRepository.getId(patientId)).thenReturn(null);

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,() -> {
            patientService.viewAppointment(appointmentId,patientId);
        });
        assertEquals(Constants.PATIENT_NOT_FOUND,resourceNotFoundException.getMessage());

    }




}