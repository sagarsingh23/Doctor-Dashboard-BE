//package com.dashboard.doctor_dashboard.repository;
//
//import com.dashboard.doctor_dashboard.entities.dtos.DoctorBasicDetailsDto;
//import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
//import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//
//@DataJpaTest
//class DoctorRepositoryTest {
//
//    @MockBean
//    private DoctorRepository doctorRepository;
//
//    @BeforeEach
//    void init(){
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @AfterEach
//    void tearDown() {
//        doctorRepository.deleteAll();
//    }
//
//
//
//
//    @Test
//    void testUpdateDoctorDb() {
//        DoctorFormDto doctorFormDto1 = new DoctorFormDto(1L,(short) 21,"orthology","male",
//                null);
//
//
//        doctorRepository.updateDoctorDb(
//                doctorFormDto1.getAge(),
//                doctorFormDto1.getSpeciality(),
//                doctorFormDto1.getGender(),
//                doctorFormDto1.getPhoneNo(),
//                doctorFormDto1.getId()
//        );
//        doctorRepository.updateDoctorDb(
//                doctorFormDto1.getAge(),
//                doctorFormDto1.getSpeciality(),
//                doctorFormDto1.getGender(),
//                doctorFormDto1.getPhoneNo(),
//                doctorFormDto1.getId()
//        );
//
//
//        verify(doctorRepository,times(2)).updateDoctorDb(
//
//                        doctorFormDto1.getAge(),
//                        doctorFormDto1.getSpeciality(),
//                        doctorFormDto1.getGender(),
//                        doctorFormDto1.getPhoneNo(),
//                        doctorFormDto1.getId()
//        );
//
//    }
//
//    @Test
//    void testGetDoctorById() {
//        final Long id = 1L;
//        DoctorFormDto doctorFormDto = new DoctorFormDto(1L,(short) 21,"orthology","male",
//                null);
//
//        Mockito.when(doctorRepository.getDoctorById(id)).thenReturn(doctorFormDto);
//
//        assertThat(doctorRepository.getDoctorById(id)).isNotNull();
//        assertEquals(doctorRepository.getDoctorById(id),doctorFormDto);
//    }
//
//    @Test
//    void testGetAllDoctors() {
//        final Long id = 1L;
//        List<DoctorListDto> list = new ArrayList<DoctorListDto>();
//        DoctorListDto doctorListDto1 = new DoctorListDto(1,"sagar","sagar@gmail.com","orthology");
//        DoctorListDto doctorListDto2 = new DoctorListDto(2,"gokul","gokul@gmail.com","orthology");
//        list.addAll(Arrays.asList(doctorListDto1,doctorListDto2));
//
//        Mockito.when(doctorRepository.getAllDoctors(id)).thenReturn(list);
//
//        assertThat(doctorRepository.getAllDoctors(id)).isNotNull();
//        assertThat(doctorRepository.getAllDoctors(id)).isEqualTo(list);
//        assertEquals(doctorRepository.getAllDoctors(id).size(),list.size());
//
//
//    }
//
//    @Test
//    void isIdAvailable() {
//        final Long id = 1L;
//
//        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(id);
//
//        System.out.println(doctorRepository.isIdAvailable(id));
//        assertThat(doctorRepository.isIdAvailable(id)).isEqualTo(id);
//    }
//
//    @Test
//    void ifIdNotAvailable() {
//        final Long id = 1L;
//
//        Mockito.when(doctorRepository.isIdAvailable(id)).thenReturn(null);
//
//        System.out.println(doctorRepository.isIdAvailable(id));
//        assertThat(doctorRepository.isIdAvailable(id)).isNull();
//    }
//
//    @Test
//    void findDoctorById() {
//        final Long id = 1L;
//        DoctorBasicDetailsDto doctorDetails = new DoctorBasicDetailsDto("Sagar","sagarssn23@gmail.com",
//                "orthology",null,"male", (short) 21);
//
//        Mockito.when(doctorRepository.findDoctorById(id)).thenReturn(doctorDetails);
//
//        assertThat(doctorRepository.findDoctorById(id)).isNotNull();
//        assertThat(doctorRepository.findDoctorById(id)).isEqualTo(doctorDetails);
//
//    }
//}