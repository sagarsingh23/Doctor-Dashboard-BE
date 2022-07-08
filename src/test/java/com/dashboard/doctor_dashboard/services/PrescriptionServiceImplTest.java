package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.entities.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entities.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.entities.model.Appointment;
import com.dashboard.doctor_dashboard.entities.model.Prescription;
import com.dashboard.doctor_dashboard.entities.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.MailErrorException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.repository.PrescriptionRepository;
import com.dashboard.doctor_dashboard.services.PdFGeneratorServiceImpl;
import com.dashboard.doctor_dashboard.services.prescription_service.PrescriptionServiceImpl;
import org.codehaus.jettison.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrescriptionServiceImplTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private PdFGeneratorServiceImpl pdFGeneratorService;

    @Mock
    private LoginRepo loginRepo;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MailService mailService;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;

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
    void addPrescription_SUCCESS() throws MessagingException, JSONException, IOException {

        MimeMessageHelper helper = mock(MimeMessageHelper.class);
        final Long appointId = 1L;
        String message = "prescription added";

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,null,null,null,null);

        Prescription prescription1 = new Prescription(1L,"pcm",5L,"before food",5L,"morning",null,null,appointment);
        Prescription prescription2 = new Prescription(2L,"dolo",5L,"before food",5L,"morning",null,null,appointment);

        List<Prescription> prescriptions = new ArrayList<>(Arrays.asList(prescription1,prescription2));

        PatientDto patientDto = new PatientDto("dentist","Dr.pranay","Vitals updated","sagar","sagarssn23@gmail.com",21,"male");
        UpdatePrescriptionDto details = new UpdatePrescriptionDto(patientDto,prescriptions,"Vitals updated","mri check",true,1L);

        Mockito.when(appointmentRepository.getId(Mockito.any(Long.class))).thenReturn(appointId);
        Mockito.when(appointmentRepository.checkStatus(appointId)).thenReturn(details.getStatus());
        Mockito.doNothing().when(mailService).mailServiceHandler(anyString(),anyString(),anyString(),anyString(),anyString());
        Mockito.doNothing().when(mailSender).send((MimeMessage) any());
        Mockito.doReturn(mimeMessage).when(mailSender).createMimeMessage();
        ResponseEntity<GenericMessage> newMessage = prescriptionService.addPrescription(appointId,details);
        assertThat(newMessage).isNotNull();
        assertEquals(message, Objects.requireNonNull(newMessage.getBody()).getData());
    }


    @Test
    void throwErrorIfMimeMessageIsNullInSendMail() throws MessagingException, JSONException, IOException {

        MimeMessageHelper helper = mock(MimeMessageHelper.class);
        final Long appointId = 1L;
        String message = "prescription added";

        Appointment appointment = new Appointment(1L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,null,null,null,null);

        Prescription prescription1 = new Prescription(1L,"pcm",5L,"before food",5L,"morning",null,null,appointment);
        Prescription prescription2 = new Prescription(2L,"dolo",5L,"before food",5L,"morning",null,null,appointment);

        List<Prescription> prescriptions = new ArrayList<>(Arrays.asList(prescription1,prescription2));

        PatientDto patientDto = new PatientDto("dentist","Dr.pranay","Vitals updated","sagar","sagarssn23@gmail.com",21,"male");
        UpdatePrescriptionDto details = new UpdatePrescriptionDto(patientDto,prescriptions,"Vitals updated","mri check",true,1L);

        Mockito.when(appointmentRepository.getId(Mockito.any(Long.class))).thenReturn(appointId);
        Mockito.when(appointmentRepository.checkStatus(appointId)).thenReturn(details.getStatus());
        Mockito.doThrow(new MailErrorException("Mail Error")).when(mailService).mailServiceHandler(anyString(),anyString(),anyString(),anyString(),anyString());


        MailErrorException mailErrorException = assertThrows(MailErrorException.class,()->{
            prescriptionService.addPrescription(appointId,details);
        });
        System.out.println(mailErrorException);
    }



    @Test
    void throwErrorIfAppointmentIdNotEqualsToPrescriptionDetails() throws MessagingException, JSONException, IOException {

        final Long appointId = 1L;
        String message = "prescription added";

        Appointment appointment = new Appointment(2L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,null,null,null,null);

        Prescription prescription1 = new Prescription(1L,"pcm",5L,"before food",5L,"morning",null,null,appointment);
        Prescription prescription2 = new Prescription(2L,"dolo",5L,"before food",5L,"morning",null,null,appointment);

        List<Prescription> prescriptions = new ArrayList<>(Arrays.asList(prescription1,prescription2));

        PatientDto patientDto = new PatientDto("dentist","Dr.pranay","Vitals updated","sagar","sagarssn23@gmail.com",21,"male");
        UpdatePrescriptionDto details = new UpdatePrescriptionDto(patientDto,prescriptions,"Vitals updated","mri check",true,1L);

        Mockito.when(appointmentRepository.getId(Mockito.any(Long.class))).thenReturn(appointId);
        Mockito.when(appointmentRepository.checkStatus(appointId)).thenReturn(details.getStatus());

        assertThrows(ResourceNotFoundException.class,()->{
             prescriptionService.addPrescription(appointId,details);
        });
    }


    @Test
    void throwErrorIfStatusIsNotVitalsUpdatedForAddPrescription() throws MessagingException, JSONException, IOException {

        final Long appointId = 1L;
        String message = "prescription added";

        Appointment appointment = new Appointment(2L,"dentist", LocalDate.now(),"fever","sagar","sagarssn23@gmal.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,true,2L,null,null,null,null);

        Prescription prescription1 = new Prescription(1L,"pcm",5L,"before food",5L,"morning",null,null,appointment);
        Prescription prescription2 = new Prescription(2L,"dolo",5L,"before food",5L,"morning",null,null,appointment);

        List<Prescription> prescriptions = new ArrayList<>(Arrays.asList(prescription1,prescription2));

        PatientDto patientDto = new PatientDto("dentist","Dr.pranay","Vitals updated","sagar","sagarssn23@gmail.com",21,"male");
        UpdatePrescriptionDto details = new UpdatePrescriptionDto(patientDto,prescriptions,"completed","mri check",true,1L);

        Mockito.when(appointmentRepository.getId(Mockito.any(Long.class))).thenReturn(appointId);
        Mockito.when(appointmentRepository.checkStatus(appointId)).thenReturn(details.getStatus());

        assertThrows(APIException.class,()->{
            prescriptionService.addPrescription(appointId,details);
        });
    }


    @Test
    void throwErrorIfIdNotPresentInAppointmentDBForAddPrescription() throws MessagingException, JSONException, IOException {
        final Long appointId = 1L;
        Prescription prescription1 = new Prescription(1L,"pcm",5L,"before food",5L,"morning",null,null,null);
        Prescription prescription2 = new Prescription(2L,"dolo",5L,"before food",5L,"morning",null,null,null);

        List<Prescription> prescriptions = new ArrayList<>(Arrays.asList(prescription1,prescription2));

        PatientDto patientDto = new PatientDto("dentist","Dr.pranay","Vitals updated","sagar","sagarssn23@gmail.com",21,"male");

        UpdatePrescriptionDto details = new UpdatePrescriptionDto(patientDto,prescriptions,"completed","mri check",true,1L);

        Mockito.when(appointmentRepository.getId(Mockito.any(Long.class))).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            prescriptionService.addPrescription(appointId,details);
        });
    }








    @Test
    void getAllPrescriptionByAppointment_SUCCESS() {
        final Long appointId = 1L;
        Prescription prescription1 = new Prescription(1L,"pcm",5L,"before food",5L,"morning",null,null,null);
        Prescription prescription2 = new Prescription(2L,"dolo",5L,"before food",5L,"morning",null,null,null);

        List<Prescription> prescriptions = new ArrayList<>(Arrays.asList(prescription1,prescription2));

        Mockito.when(appointmentRepository.getId(Mockito.any(Long.class))).thenReturn(appointId);
        Mockito.when(prescriptionRepository.getAllPrescriptionByAppointment(appointId)).thenReturn(prescriptions);

        ResponseEntity<GenericMessage> newPrescriptionList = prescriptionService.getAllPrescriptionByAppointment(appointId);
        assertThat(newPrescriptionList).isNotNull();
        assertEquals(prescriptions,newPrescriptionList.getBody().getData());
    }

    @Test
    void throwErrorIfIdNotPresentInAppointmentDb() {
        final Long appointId = 1L;

        Mockito.when(appointmentRepository.getId(Mockito.any(Long.class))).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,()->{
            prescriptionService.getAllPrescriptionByAppointment(appointId);
        });
    }

    @Test
    void deleteAppointmentById() {
        final Long id = 1L;

        prescriptionService.deleteAppointmentById(id);
        prescriptionService.deleteAppointmentById(id);

        verify(prescriptionRepository,times(2)).deleteById(id);
    }
}