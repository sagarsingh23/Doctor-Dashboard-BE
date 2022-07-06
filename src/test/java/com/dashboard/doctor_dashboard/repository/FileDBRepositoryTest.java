//package com.dashboard.doctor_dashboard.repository;
//
//import com.dashboard.doctor_dashboard.entities.report.FileDB;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@DataJpaTest
//class FileDBRepositoryTest {
//
//    @MockBean
//    private FileDBRepository fileDBRepository;
//
//    @BeforeEach
//    void init(){
//        MockitoAnnotations.openMocks(this);
//        System.out.println("setting up");
//    }
//
//    @AfterEach
//    void tearDown() {
//        System.out.println("tearing down..");
//        fileDBRepository.deleteAll();
//    }
//
//
//    @Test
//    void findByPatientId() {
//        final Long id = 1L;
//        FileDB fileDB = new FileDB();
//        fileDB.setDataReport(null);
//        fileDB.setId(id);
//        fileDB.setType(".png");
//        fileDB.setName("file1");
//        fileDB.setPatientId(id);
//
//        Mockito.when(fileDBRepository.findByPatientId(id)).thenReturn(fileDB);
//
//        assertThat(fileDBRepository.findByPatientId(id)).isNotNull();
//        assertThat(fileDBRepository.findByPatientId(id)).isEqualTo(fileDB);
//
//    }
//}