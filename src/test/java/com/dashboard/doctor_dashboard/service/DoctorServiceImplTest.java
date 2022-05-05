package com.dashboard.doctor_dashboard.service;

import com.dashboard.doctor_dashboard.entity.DoctorDetails;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entity.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.service.doctor_service.DoctorServiceImpl;
import com.dashboard.doctor_dashboard.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;


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
    void testAddDoctor() {
        DoctorDetails newDoctor = new DoctorDetails();
        newDoctor.setId(1L);
        newDoctor.setFirstName("Sagar");
        newDoctor.setLastName("Negi");
        newDoctor.setEmail("sagarssn23@gmail.com");
        DoctorDetails doctorDetails = new DoctorDetails(1L,"Sagar","Singh", (short) 21,
                "sagarssn23@gmail.com","orthology",
                null,"male",null,null);

        Mockito.doReturn(doctorDetails).when(doctorRepository).save(Mockito.any(DoctorDetails.class));

        DoctorDetails newDoctorDetails = doctorService.addDoctor(doctorDetails);

        assertThat(newDoctorDetails).isNotNull();
        verify(doctorRepository).save(Mockito.any(DoctorDetails.class));
    }

    @Test
    void testGetAllDoctors() {
        final Long id = 1L;
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(id);
        Mockito.when(doctorRepository.getAllDoctors(id)).thenReturn(list);

        List<DoctorListDto> newList = doctorService.getAllDoctors(id);

        assertEquals(list.size(),newList.size());
        assertEquals(doctorListDto1.getName(),newList.get(0).getName());
        assertEquals(doctorListDto1.getEmail(),newList.get(0).getEmail());
        assertEquals(doctorListDto1.getId(),newList.get(0).getId());
        assertEquals(doctorListDto2.getName(),newList.get(1).getName());
        assertEquals(doctorListDto2.getEmail(),newList.get(1).getEmail());
        assertEquals(doctorListDto2.getId(),newList.get(1).getId());
    }

    @Test
    void throwErrorIfIdNotPresentInDb() {
        final Long id = 1L;
        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com");
        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com");
        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.getAllDoctors(id);
        });
    }


    @Test
    void getDoctorById() {
        Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21);

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(id);
        Mockito.when(doctorRepository.findDoctorById(id)).thenReturn(doctorDetails);

        DoctorBasicDetailsDto newDetails = doctorService.getDoctorById(id);

        assertThat(newDetails).isNotNull();
        assertEquals(doctorDetails.getFirstName(),newDetails.getFirstName());
        assertEquals(doctorDetails.getEmail(),newDetails.getEmail());
    }

    @Test
    void throwErrorWhenIdNoPresent() {
        Long id = 1L;
        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
                "orthology",null,"male", (short) 21);

        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);

        System.out.println(doctorService.getDoctorById(id));

        assertEquals(null,doctorService.getDoctorById(id));
        verify(doctorRepository,never()).findDoctorById(id);
    }

    @Test
    void updateDoctor() {
        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);

        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(doctorFormDto.getId());
        doctorService.updateDoctor(doctorFormDto,doctorFormDto.getId());
        doctorService.updateDoctor(doctorFormDto,doctorFormDto.getId());

        verify(doctorRepository,times(2))
                .updateDoctorDb(
                        doctorFormDto.getAge(),
                        doctorFormDto.getSpeciality(),
                        doctorFormDto.getGender(),
                        doctorFormDto.getPhoneNo(),
                        doctorFormDto.getId()
                        );

    }

    @Test
    void IfIdMisMatchForUpdateDoctor() {
        Long id1 = 1L;
        Long id2 = 2L;
        DoctorFormDto doctorFormDto = new DoctorFormDto(id1,(short) 21,"orthology","male",
                null);

        Mockito.when(doctorRepository.isIdAvailable(id2))
                .thenReturn(id2);

       DoctorFormDto newDetails = doctorService.updateDoctor(doctorFormDto,id2);

       assertEquals(null,newDetails);
        verify(doctorRepository,never()).updateDoctorDb(
                doctorFormDto.getAge(),
                doctorFormDto.getSpeciality(),
                doctorFormDto.getGender(),
                doctorFormDto.getPhoneNo(),
                doctorFormDto.getId()
        );
    }

    @Test
    void ThrowErrorIfIdNotPresentForUpdateDoctorInDatabase() {
        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
                null);

        Mockito.when(doctorRepository.isIdAvailable(doctorFormDto.getId()))
                .thenReturn(null);

        Long id=doctorFormDto.getId();
        assertThrows(ResourceNotFoundException.class,()->{
            doctorService.updateDoctor(doctorFormDto,id);
        });

        verify(doctorRepository,never()).updateDoctorDb(
                doctorFormDto.getAge(),
                doctorFormDto.getSpeciality(),
                doctorFormDto.getGender(),
                doctorFormDto.getPhoneNo(),
                doctorFormDto.getId()
        );
    }

    @Test
    void testDeleteDoctor() {
        final Long id = 1L;

        doctorService.deleteDoctor(id);

        doctorService.deleteDoctor(id);

        verify(doctorRepository,times(2)).deleteById(id);
    }
}