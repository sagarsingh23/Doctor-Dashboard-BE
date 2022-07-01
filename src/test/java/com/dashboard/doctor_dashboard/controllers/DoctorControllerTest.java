package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFound;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorService;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.exceptions.ValidationsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    void  getAllDoctors() {
        final Long id = 1L;
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthology",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthology",(short)6,"MBBS");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,list);


        Mockito.when(doctorService.getAllDoctors(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        ResponseEntity<GenericMessage> newList = doctorController.getAllDoctors(id);
        assertThat(newList).isNotNull();
        assertEquals(list,newList.getBody().getData());
    }

    @Test
    void throwErrorIfIdNotPresentDbForAllDoctor() {
        final Long id = 1L;
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthology",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthology",(short)6,"MBBS");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        Mockito.when(doctorService.getAllDoctors(Mockito.any(Long.class))).thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorController.getAllDoctors(id);
        });

    }

    @Test
    void getDoctorsByIdIfIdPresent() {
        final Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21,"MBBS",(short)8);

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,doctorDetails);


        Mockito.when(doctorService.getDoctorById(id)).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        ResponseEntity<GenericMessage> newDetails = doctorController.getDoctorById(1);
        System.out.println(newDetails);

        assertEquals(doctorDetails,newDetails.getBody().getData());
    }

    @Test
    void throwErrorIfIdNotPresentDb() {
        final Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21,"MBBS",(short)8);

        Mockito.when(doctorService.getDoctorById(id)).thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorController.getDoctorById(id);
        });

    }

    @Test
    void addDoctorDetails() {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,doctorFormDto);


        Mockito.when(doctorService.addDoctorDetails(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));


        ResponseEntity<GenericMessage> newDoctorDetails = doctorController.addDoctorDetails(1,doctorFormDto,result,request);
        System.out.println(newDoctorDetails);
        assertEquals(200,newDoctorDetails.getStatusCodeValue());
        assertEquals(doctorFormDto,newDoctorDetails.getBody().getData());
    }

    @Test
    void checkIfAddDoctorDetailsHasError() {
        BindingResult result = mock(BindingResult.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        ObjectError error = new ObjectError("age","age should be between 24-100");
        result.addError(error);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");

        Mockito.when(result.hasErrors()).thenReturn(true);

        WebRequest webRequest = mock(WebRequest.class);


        assertThrows(ValidationsException.class,()->{
            doctorController.addDoctorDetails(1,doctorFormDto,result,request);
        });

    }

    @Test
    void throwErrorIfIdMisMatchForAddDoctor() {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);


        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");

        Mockito.when(doctorService.addDoctorDetails(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(null);

        assertThrows(APIException.class,()->{
            doctorController.addDoctorDetails(1,doctorFormDto,result,request);
        });

    }



    @Test
    void updateDoctorDetails() {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,doctorFormDto);


        Mockito.when(doctorService.updateDoctor(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));


        ResponseEntity<GenericMessage> newDoctorDetails = doctorController.updateDoctorDetails(1,doctorFormDto,result,request);
        System.out.println(newDoctorDetails);
        assertEquals(200,newDoctorDetails.getStatusCodeValue());
        assertEquals(doctorFormDto,newDoctorDetails.getBody().getData());
    }

    @Test
    void checkIfUpdateDoctorDetailsHasError() {
        BindingResult result = mock(BindingResult.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        ObjectError error = new ObjectError("age","age should be between 24-100");
        result.addError(error);

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");

        Mockito.when(result.hasErrors()).thenReturn(true);

        WebRequest webRequest = mock(WebRequest.class);


        assertThrows(ValidationsException.class,()->{
            doctorController.updateDoctorDetails(1,doctorFormDto,result,request);
        });

    }

    @Test
    void throwErrorIfIdMisMatchForUpdateDoctor() {
        BindingResult result = mock(BindingResult.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);


        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");

        Mockito.when(doctorService.updateDoctor(Mockito.any(DoctorFormDto.class),
                Mockito.any(Long.class),Mockito.any())).thenReturn(null);

        assertThrows(APIException.class,()->{
            doctorController.updateDoctorDetails(1,doctorFormDto,result,request);
        });

    }

    @Test
    void deleteDoctor() {
//        DoctorDetails doctorDetails = new DoctorDetails(1L,(short) 26,"orthology","male",
//                "9728330045",(short)6,"MBBS",null,null,null,1L);

//        doctorDetails.toString();


        GenericMessage message  = new GenericMessage(Constants.SUCCESS,"Deleted");

        Mockito.when(doctorService.deleteDoctor(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        ResponseEntity<GenericMessage> res = doctorController.deleteDoctor(1);

        assertEquals("Deleted",res.getBody().getData());

    }

    @Test
    void getAllDoctorsBySpecialityTest() {
        final String speciality = "orthology";
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthology",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthology",(short)6,"MBBS");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,list);


        Mockito.when(doctorService.getAllDoctorsBySpeciality(Mockito.any(String.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        ResponseEntity<GenericMessage> getList = doctorController.getAllDoctorsBySpeciality(speciality);
        assertThat(getList).isNotNull();
        assertEquals(list,getList.getBody().getData());
    }

    @Test
    void genderChartTest() {

        final Long id = 1L;
        Map<String,Integer> m = new HashMap<>();
        m.put("Male",2);
        m.put("Female",5);

        GenericMessage message = new GenericMessage(Constants.SUCCESS,m);

        Mockito.when(doctorService.genderChart(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        ResponseEntity<GenericMessage> getGenderChart = doctorController.gender(id);
        assertThat(getGenderChart).isNotNull();
        assertEquals(m,getGenderChart.getBody().getData());


    }

    @Test
    void bloodGroupChartTest() {

        final Long id = 1L;
        Map<String,Integer> m = new HashMap<>();
        m.put("A+",2);
        m.put("AB-",5);

        GenericMessage message = new GenericMessage(Constants.SUCCESS,m);

        Mockito.when(doctorService.bloodGroupChart(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        ResponseEntity<GenericMessage> getBloodGroupChart = doctorController.bloodGroup(id);
        assertThat(getBloodGroupChart).isNotNull();
        assertEquals(m,getBloodGroupChart.getBody().getData());


    }

    @Test
    void ageGroupChartTest() {

        final Long id = 1L;
        Map<String,Integer> m = new HashMap<>();
        m.put("0-2",1);
        m.put("3-14",2);
        m.put("26-64",7);


        GenericMessage message = new GenericMessage(Constants.SUCCESS,m);

        Mockito.when(doctorService.ageGroupChart(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message,HttpStatus.OK));

        ResponseEntity<GenericMessage> getAgeGroupChart = doctorController.ageGroup(id);
        assertThat(getAgeGroupChart).isNotNull();
        assertEquals(m,getAgeGroupChart.getBody().getData());

    }
}