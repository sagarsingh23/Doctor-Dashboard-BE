package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.entities.report.FileDB;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.FileDBRepository;
import com.dashboard.doctor_dashboard.services.file_service.FileStorageServiceImpl;
import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import org.apache.commons.lang.ObjectUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileStorageServiceImplTest {

    @Mock
    private FileDBRepository fileDBRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private FileStorageServiceImpl fileStorageServiceImpl;


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
    void UploadFileWHenPatientIdPresentInDb() throws Exception {


        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(null);
        fileDB.setId(id);
        fileDB.setType(".png");
        fileDB.setName("file1");
        fileDB.setAppointmentId(id);

        MultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        Mockito.when(appointmentRepository.getId(id)).thenReturn(id);
        Mockito.when(fileDBRepository.save(Mockito.any(FileDB.class))).thenReturn(fileDB);


        ResponseEntity<GenericMessage> response = fileStorageServiceImpl.store(file,id);

        assertAll(
                ()-> assertThat(response).isNotNull(),
                ()-> assertEquals(Constants.FILE_UPLOADED+file.getOriginalFilename(),response.getBody().getData())
        );
    }

    @Test
    void CheckIfFileNameIsPresentOrNot() throws IOException {

        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(null);
        fileDB.setId(id);
        fileDB.setType(".png");
        fileDB.setName("file1");
        fileDB.setAppointmentId(id);

        MultipartFile file = mock(MultipartFile.class);

        Mockito.when(appointmentRepository.getId(id)).thenReturn(id);
        Mockito.when(file.getOriginalFilename()).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,()->{
            fileStorageServiceImpl.store(file,id);
        });

        assertAll(
                ()-> assertThat(exception).isNotNull(),
                ()-> assertEquals(Constants.FILE_NAME_PRESENT,exception.getMessage())
        );
    }

    @Test
    void ThrowErrorWhenAppointmentIdNotPresentInDb(){

        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(null);
        fileDB.setId(id);
        fileDB.setType(".png");
        fileDB.setName("file1");
        fileDB.setAppointmentId(id);

        MultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        Mockito.when(appointmentRepository.getId(id)).thenReturn(null);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,()->{
            fileStorageServiceImpl.store(file,id);
        });

        assertAll(
                ()-> assertThat(exception).isNotNull(),
                ()-> assertEquals(Constants.APPOINTMENT_NOT_FOUND,exception.getMessage())
        );
    }


//    @Test
//    void throwsException() throws IOException {
//        final Long id = 1L;
//        InputStream in = mock(InputStream.)
//
//        MultipartFile file1 = new MockMultipartFile(
//                "file"," j bjhbhj",
//                String.valueOf(MediaType.TEXT_PLAIN),
//                null
//        );
////        MockMultipartFile file = mock(MockMultipartFile.class);
//        Mockito.when(appointmentRepository.getId(id)).thenReturn(id);
//       // Mockito.when(file.getBytes()).thenThrow(IOException.class);
//        System.out.println("empty"+file1.getBytes());
//        Exception exception = assertThrows(Exception.class,()->{
//            fileStorageServiceImpl.store(file1,id);
//        });
//        System.out.println(exception);
//
//    }


    @Test
    void getFile() throws IOException {

        MultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(file.getBytes());
        fileDB.setId(id);
        fileDB.setType(".png");
        fileDB.setName("file1");
        fileDB.setAppointmentId(id);


        Mockito.when(fileDBRepository.findByAppointmentId(id)).thenReturn(fileDB);

        ResponseEntity<byte[]> response = fileStorageServiceImpl.getFile(id);

        assertAll(
                ()->assertThat(response).isNotNull(),
                ()->assertEquals(fileDB.getDataReport(),response.getBody())
        );
    }


    @Test
    void throwErrorWhenIdNotPresentInDbForReport() throws Exception {
        final Long id = 1L;

        Mockito.when(fileDBRepository.findByAppointmentId(id)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,()->{
            fileStorageServiceImpl.getFile(id);
        });

        assertAll(
                ()-> assertThat(exception).isNotNull(),
                ()-> assertEquals(Constants.REPORT_NOT_FOUND,exception.getMessage())
        );


    }


}

