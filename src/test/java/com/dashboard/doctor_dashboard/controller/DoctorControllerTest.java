package com.dashboard.doctor_dashboard.controller;

import com.dashboard.doctor_dashboard.entity.DoctorDetails;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.service.doctor_service.DoctorService;
import com.dashboard.doctor_dashboard.exception.APIException;
import com.dashboard.doctor_dashboard.exception.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.exception.ValidationsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DoctorControllerTest {


    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;


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
    void getAllDoctors() {
        final Long id = 1L;
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        Mockito.when(doctorService.getAllDoctors(Mockito.any(Long.class))).thenReturn(list);

        List<DoctorListDto> newList = doctorController.getAllDoctors(id);
        System.out.println(newList);

        assertEquals(list.size(),newList.size());
        assertEquals(doctorListDto1.getName(),newList.get(0).getName());
        assertEquals(doctorListDto2.getName(),newList.get(1).getName());
    }

    @Test
    void throwErrorIfIdNotPresentDbForAllDoctor() {
        final Long id = 1L;
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        Mockito.when(doctorService.getAllDoctors(Mockito.any(Long.class))).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorController.getAllDoctors(id);
        });

    }

    @Test
    void getDoctorsByIdIfIdPresent() {
        final Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21);

        Mockito.when(doctorService.getDoctorById(id)).thenReturn(doctorDetails);

        DoctorBasicDetailsDto newDetails = doctorController.getDoctorById(1);
        System.out.println(newDetails);

        assertEquals(doctorDetails.getFirstName(),newDetails.getFirstName());
        assertEquals(doctorDetails.getEmail(),newDetails.getEmail());
        assertEquals(doctorDetails.getSpeciality(),newDetails.getSpeciality());
        assertEquals(doctorDetails.getGender(),newDetails.getGender());
        assertEquals(doctorDetails.getPhoneNo(),newDetails.getPhoneNo());
        assertEquals(doctorDetails.getAge(),newDetails.getAge());

    }

    @Test
    void throwErrorIfIdNotPresentDb() {
        final Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21);

        Mockito.when(doctorService.getDoctorById(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorController.getDoctorById(id);
        });

    }


    @Test
    void updateDoctorDetails() {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);

        Mockito.when(doctorService.updateDoctor(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class))).thenReturn(doctorFormDto);


        ResponseEntity<DoctorFormDto> newDoctorDetails = doctorController.updateDoctorDetails(1,doctorFormDto,result,webRequest);
        System.out.println(newDoctorDetails);
        assertEquals(200,newDoctorDetails.getStatusCodeValue());
        assertEquals(doctorFormDto.getId(),newDoctorDetails.getBody().getId());
        assertEquals(doctorFormDto.getSpeciality(),newDoctorDetails.getBody().getSpeciality());
    }

    @Test
    void checkIfUpdateDoctorDetailsHasError() {
        BindingResult result = mock(BindingResult.class);
        ObjectError error = new ObjectError("age","age should be between 24-100");
        result.addError(error);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);

        Mockito.when(result.hasErrors()).thenReturn(true);

        WebRequest webRequest = mock(WebRequest.class);


        assertThrows(ValidationsException.class,()->{
            doctorController.updateDoctorDetails(1,doctorFormDto,result,webRequest);
        });

    }

    @Test
    void throwErrorIfIdMisMatchForUpdateDoctor() {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);


        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);

        Mockito.when(doctorService.updateDoctor(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class))).thenReturn(null);

                assertThrows(APIException.class,()->{
            doctorController.updateDoctorDetails(1,doctorFormDto,result,webRequest);
        });

    }

    @Test
    void deleteDoctor() {
        DoctorDetails doctorDetails = new DoctorDetails(1L,"Sagar","Singh", (short) 21,
                "sagarssn23@gmail.com","orthology",
                null,"male",null,null);


        String value = "deleted";
        Mockito.when(doctorService.deleteDoctor(Mockito.any(Long.class))).thenReturn(value);

        String res = doctorController.deleteDoctor(1);

        assertEquals(value,res);

    }
}