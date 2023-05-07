package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.dtos.AttributesDto;
import com.dashboard.doctor_dashboard.dtos.DoctorDropdownDto;
import com.dashboard.doctor_dashboard.dtos.PatientViewDto;
import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.enums.Category;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.services.impl.ReceptionistServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ReceptionistServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AttributeRepository attributeRepository;

    @InjectMocks
    private ReceptionistServiceImpl receptionistService;


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
    void getDoctorDetails_SUCCESS() {
        DoctorDropdownDto dto1 = new DoctorDropdownDto(1L,"Sagar","sagar@gmail.com", Category.Orthologist);
        DoctorDropdownDto dto2 = new DoctorDropdownDto(2L,"pranay","pranay@gmail.com",Category.General);
        List<DoctorDropdownDto> list = new ArrayList<>(Arrays.asList(dto1, dto2));

        Mockito.when(doctorRepository.getDoctorDetails()).thenReturn(list);

        ResponseEntity<GenericMessage> newList = receptionistService.getDoctorDetails();
        System.out.println(newList);
        assertThat(newList).isNotNull();
        assertEquals(list, Objects.requireNonNull(newList.getBody()).getData());
    }

    @Test
    void getDoctorAppointments_SUCCESS() {

        final Long doctorId = 1L;
        int pageNo = 2;
        int pageSize = 11;
        Pageable paging= PageRequest.of(pageNo, pageSize);


        PatientViewDto dto1 = new PatientViewDto(1L, LocalTime.now(),"sagar","sagar@gmail.com","completed");
        List<PatientViewDto> list = new ArrayList<>(Arrays.asList(dto1,dto1));

        Appointment appointment = new Appointment(1L,Category.Dentist, LocalDate.now(),"fever","sagar","sagar@gmail.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,false,true,2L,null,null,null,null);

        List<Appointment> appointmentList = new ArrayList<>(Arrays.asList(appointment,appointment));
        Page<Appointment> list1=new PageImpl<>(appointmentList);


        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(doctorId);
        Mockito.when(appointmentRepository.receptionistDoctorAppointment(doctorId,paging)).thenReturn(list1);
        Mockito.when(mapper.map(appointment,PatientViewDto.class)).thenReturn(dto1);

        ResponseEntity<GenericMessage> newList = receptionistService.getDoctorAppointments(doctorId,pageNo,pageSize);
        System.out.println(newList);
        assertThat(newList).isNotNull();
        assertEquals(list, Objects.requireNonNull(newList.getBody()).getData());

    }

    @Test
    void throwErrorIfIdNotPresentInDoctorDbForDoctorAppointment() {
        final Long doctorId = 1L;
        int pageNo = 2;
        int pageSize = 11;

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            receptionistService.getDoctorAppointments(doctorId,pageNo,pageSize);
        });
    }

    @Test
    void todayAllAppointmentForClinicStaff_SUCCESS() {
        int pageNo = 2;
        int pageSize = 11;

        Pageable paging= PageRequest.of(pageNo, pageSize);


        PatientViewDto dto1 = new PatientViewDto(1L, LocalTime.now(),"sagar","sagar@gmail.com","completed");
        Appointment appointment = new Appointment(1L,Category.Dentist, LocalDate.now(),"fever","sagar","sagar@gmail.com",
                "pranay", LocalTime.now(),true,"completed",null,null,null,false,true,2L,null,null,null,null);

        List<PatientViewDto> list = new ArrayList<>(Arrays.asList(dto1,dto1,dto1,dto1));
        List<Appointment> appointmentList = new ArrayList<>(Arrays.asList(appointment,appointment));
        Page<Appointment> list1=new PageImpl<>(appointmentList);


        Mockito.when(appointmentRepository.todayAllAppointmentForClinicStaff1(paging)).thenReturn(list1);
        Mockito.when(appointmentRepository.todayAllAppointmentForClinicStaff2(paging)).thenReturn(list1);
        Mockito.when(mapper.map(appointment,PatientViewDto.class)).thenReturn(dto1);

        ResponseEntity<GenericMessage> newList = receptionistService.todayAllAppointmentForClinicStaff(pageNo,pageSize);
        assertThat(newList).isNotNull();
        assertEquals(list,newList.getBody().getData());
    }

    @Test
    void addAppointmentVitals_SUCCESS() {
        final Long appointId = 1L;
        String message = "successful";
        AttributesDto attributes = new AttributesDto("120/80",100L,99D,"mri check",null);


        Mockito.when(appointmentRepository.existsById(appointId)).thenReturn(true);
        Mockito.when(attributeRepository.checkAppointmentPresent(appointId)).thenReturn(null);

        ResponseEntity<GenericMessage> newMessage = receptionistService.addAppointmentVitals(attributes,appointId);
        assertThat(newMessage).isNotNull();
        assertEquals(message, Objects.requireNonNull(newMessage.getBody()).getData());

    }

    @Test
    void throwErrorIFIdNotPresentInAppointmentDbForAppointmentVitals() {
        final Long appointId = 1L;
        AttributesDto attributes = new AttributesDto("120/80",100L,99D,"mri check",null);

        Mockito.when(appointmentRepository.existsById(appointId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,()->{
            receptionistService.addAppointmentVitals(attributes,appointId);
        });

    }

    @Test
    void throwErrorIFIdNotPresentInAttributeDbForAppointmentVitals() {
        final Long appointId = 1L;
        String message = "successful";
        AttributesDto attributes = new AttributesDto("120/80",100L,99D,"mri check",null);


        Mockito.when(appointmentRepository.existsById(appointId)).thenReturn(true);
        Mockito.when(attributeRepository.checkAppointmentPresent(appointId)).thenReturn(appointId);

        assertThrows(APIException.class,()->{
            receptionistService.addAppointmentVitals(attributes,appointId);
        });
    }



}