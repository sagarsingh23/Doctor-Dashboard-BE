package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFound;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private LoginRepo loginRepo;

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @InjectMocks
    private DoctorServiceImpl doctorService;



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
    void testGetAllDoctors() {
        final Long id = 1L;
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthology",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthology",(short)6,"MBBS");
        List<DoctorListDto> list = new ArrayList<>(Arrays.asList(doctorListDto1, doctorListDto2));


        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(id);
        Mockito.when(doctorRepository.getAllDoctors(Mockito.any(Long.class))).thenReturn(list);

        ResponseEntity<GenericMessage> newList = doctorService.getAllDoctors(id);

        assertThat(newList).isNotNull();
        assertEquals(list,newList.getBody().getData());
        assertEquals(Constants.SUCCESS,newList.getBody().getStatus());
    }

    @Test
    void throwErrorIfIdNotPresentInDb() {
        final Long id = 1L;
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthology",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthology",(short)6,"MBBS");
        List<DoctorListDto> list = new ArrayList<>(Arrays.asList(doctorListDto1, doctorListDto2));

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.getAllDoctors(id);
        });
    }


    @Test
    void getDoctorById() {
        Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21,"MBBS",(short)8);

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(id);
        Mockito.when(doctorRepository.findDoctorById(Mockito.any(Long.class))).thenReturn(doctorDetails);

        ResponseEntity<GenericMessage> newDetails = doctorService.getDoctorById(id);

        assertThat(newDetails).isNotNull();
        assertEquals(doctorDetails,newDetails.getBody().getData());
    }

    @Test
    void throwErrorWhenIdNoPresent() {
        Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21,"MBBS",(short)8);

        Mockito.when(doctorRepository.isIdAvailable(Mockito.any(Long.class))).thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.getDoctorById(id);
        });
    }


    @Test
    void addDoctorTest_SUCCESS() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long tokenId = 1L;
        Long loginId = 1L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(tokenId);
        Mockito.when(loginRepo.isIdAvailable(tokenId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId())).thenReturn(null);

        doctorService.addDoctorDetails(doctorFormDto,doctorFormDto.getId(),request);
        doctorService.addDoctorDetails(doctorFormDto,doctorFormDto.getId(),request);

        verify(doctorRepository,times(2))
                .insertARowIntoTheTable(
                        doctorFormDto.getId(),
                        doctorFormDto.getAge(),
                        doctorFormDto.getSpeciality(),
                        doctorFormDto.getPhoneNo(),
                        doctorFormDto.getGender(),
                        1L,
                        doctorFormDto.getExp(),
                        doctorFormDto.getDegree()
                );

    }

    @Test
    void throwErrorDetailsNotEqualsWithTheIdProvidedForAddDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        long id = 1L;
        Long doctorLoginId = 1L;
        Long loginId = 1L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(4L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.addDoctorDetails(doctorFormDto,id,request);
        });

    }
    @Test
    void throwErrorIfDetailsIdMisMatchForAddDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        long id = 1L;
        Long doctorLoginId = 2L;
        Long loginId = 1L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(4L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.addDoctorDetails(doctorFormDto,id,request);
        });

    }

    @Test
    void throwErrorDetailsNotEqualsWithTheDoctorLoginIdForAddDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        long id = 1L;
        Long doctorLoginId = 2L;
        Long loginId = 1L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.addDoctorDetails(doctorFormDto,id,request);
        });

    }

    @Test
    void ThrowErrorIfIdNotPresentInDoctorDetailsForAddDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id=1L;
        Long id2 = 2L;

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(id);

        Mockito.when(doctorRepository.isIdAvailable(id))
                .thenReturn(id2);


        assertThrows(APIException.class,()->{
            doctorService.addDoctorDetails(doctorFormDto,id,request);
        });
    }


    @Test
    void ThrowErrorIfIdNotPresentInLoginDetailsForAddDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id=1L;

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(id);

        Mockito.when(loginRepo.isIdAvailable(id))
                .thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.addDoctorDetails(doctorFormDto,id,request);
        });
    }



    @Test
    void updateDoctorTest_SUCCESS() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id = 1L;
        Long doctorLoginId = 1L;
        Long loginId = 1L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(doctorFormDto.getId());
        doctorService.updateDoctor(doctorFormDto,id,request);
        doctorService.updateDoctor(doctorFormDto,id,request);

        verify(doctorRepository,times(2))
                .updateDoctorDb(doctorFormDto.getPhoneNo());

    }





    @Test
    void throwErrorDetailsNotEqualsWithTheIdProvidedForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        long id = 1L;
        Long doctorLoginId = 1L;
        Long loginId = 1L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(4L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(doctorFormDto.getId());

        assertThrows(ResourceNotFound.class,()->{
           doctorService.updateDoctor(doctorFormDto,id,request);
        });

    }
    @Test
    void throwErrorIfDetailsIdMisMatchForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        long id = 1L;
        Long doctorLoginId = 2L;
        Long loginId = 1L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(4L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(doctorFormDto.getId());

        assertThrows(ResourceNotFound.class,()->{
            doctorService.updateDoctor(doctorFormDto,id,request);
        });

    }

    @Test
    void throwErrorDetailsNotEqualsWithTheDoctorLoginIdForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        long id = 1L;
        Long doctorLoginId = 2L;
        Long loginId = 1L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(request)).thenReturn(doctorLoginId);
        Mockito.when(loginRepo.isIdAvailable(doctorLoginId)).thenReturn(loginId);
        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(doctorFormDto.getId());

        assertThrows(ResourceNotFound.class,()->{
            doctorService.updateDoctor(doctorFormDto,id,request);
        });

    }

    @Test
    void ThrowErrorIfIdNotPresentInLoginDetailsForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id=1L;

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(Mockito.any())).thenReturn(id);

        Mockito.when(loginRepo.isIdAvailable(id))
                .thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.updateDoctor(doctorFormDto,id,request);
        });
    }

    @Test
    void ThrowErrorIfIdNotPresentInDoctorDetailsForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id=1L;

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(Mockito.any())).thenReturn(id);

        Mockito.when(loginRepo.isIdAvailable(Mockito.any(Long.class)))
                .thenReturn(id);
        Mockito.when(doctorRepository.isIdAvailable(id))
                .thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.updateDoctor(doctorFormDto,id,request);
        });
    }

    @Test
    void ThrowErrorIfIdNotPresentInDatabaseForUpdateDoctor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long id=1L;

        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 26,"orthology","male",
                "9728330045",(short)6,"MBBS");
        Mockito.when(jwtTokenProvider.getIdFromToken(Mockito.any())).thenReturn(id);

        Mockito.when(loginRepo.isIdAvailable(id))
                .thenReturn(null);
        Mockito.when(doctorRepository.isIdAvailable(id))
                .thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.updateDoctor(doctorFormDto,id,request);
        });
    }





    @Test
    void testDeleteDoctor() {
        final Long id = 1L;

        doctorService.deleteDoctor(id);
        doctorService.deleteDoctor(id);

        verify(doctorRepository,times(2)).deleteById(id);
    }

    @Test
    void getAllDoctorsBySpeciality() {
        final String speciality = "orthologist";
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","profile1","orthology",(short)8,"MBBS");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","profile2","orthology",(short)6,"MBBS");
        List<DoctorListDto> list = new ArrayList<>(Arrays.asList(doctorListDto1, doctorListDto2));

        Mockito.when(doctorRepository.isSpecialityAvailable(speciality)).thenReturn(speciality);
        Mockito.when(doctorRepository.getAllDoctorsBySpeciality(Mockito.any(String.class))).thenReturn(list);

        ResponseEntity<GenericMessage> newList = doctorService.getAllDoctorsBySpeciality(speciality);

        assertThat(newList).isNotNull();
        assertEquals(list,newList.getBody().getData());
    }

    @Test
    void throwErrorNoDoctorIsPresentBySpeciality() {
        final String speciality = "orthologist";

        Mockito.when(doctorRepository.isSpecialityAvailable(speciality)).thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.getAllDoctorsBySpeciality(speciality);
        });
    }

    @Test
    void genderChart() {
        final Long id = 1L;
        List<String> chart = new ArrayList<>(Arrays.asList("Male","female","female"));
        Map<String,Integer> m = new HashMap<>();
        m.put("Male",1);
        m.put("female",2);


        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(id);
        Mockito.when(doctorRepository.genderChart(Mockito.any(Long.class))).thenReturn(chart);

        ResponseEntity<GenericMessage> newList = doctorService.genderChart(id);
        assertThat(newList).isNotNull();
        assertEquals(m,newList.getBody().getData());
    }


    @Test
    void throwErrorIfIdNotPresentForGenderChart(){
        final Long id = 1L;

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.genderChart(id);
        });

    }


    @Test
    void bloodGroupChart() {
        final Long id = 1L;
        List<String> bloodChart = new ArrayList<>(Arrays.asList("A+","B+","A+"));
        Map<String,Integer> m = new HashMap<>();
        m.put("A+",2);
        m.put("B+",1);

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(id);
        Mockito.when(doctorRepository.bloodGroupChart(Mockito.any(Long.class))).thenReturn(bloodChart);

        ResponseEntity<GenericMessage> newList = doctorService.bloodGroupChart(id);

        assertThat(newList).isNotNull();
        assertEquals(m,newList.getBody().getData());
    }

    @Test
    void throwErrorIfIdNotPresentForBloodGroupChart(){
        final Long id = 1L;

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.bloodGroupChart(id);
        });
    }

    @Test
    void ageGroupChart() {
        final Long id = 1L;
        List<Long> ageChart = new ArrayList<>(Arrays.asList(2L,24L,35L,64L,12L,78L));
        Map<String,Integer> chart = new HashMap<>();
        var week1 = "0-2";
        var week2 = "3-14" ;
        var week3 = "15-25";
        var week4 = "26-64";
        var week5 = "65+";

        chart.put(week1,1);
        chart.put(week2,1);
        chart.put(week3,1);
        chart.put(week4,2);
        chart.put(week5,1);

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(id);
        Mockito.when(doctorRepository.ageGroupChart(Mockito.any(Long.class))).thenReturn(ageChart);

        ResponseEntity<GenericMessage> newAgeChart = doctorService.ageGroupChart(id);
        assertThat(newAgeChart).isNotNull();
        assertEquals(chart,newAgeChart.getBody().getData());

    }


    @Test
    void throwErrorIfIdNotPresentForAgeGroupChart(){
        final Long id = 1L;

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        assertThrows(ResourceNotFound.class,()->{
            doctorService.ageGroupChart(id);
        });
    }


}