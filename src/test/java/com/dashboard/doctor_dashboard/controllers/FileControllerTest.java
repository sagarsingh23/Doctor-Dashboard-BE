package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.report.FileDB;
import com.dashboard.doctor_dashboard.entities.report.ResponseMessage;
import com.dashboard.doctor_dashboard.services.patient_service.impl.FileStorageService;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FileControllerTest {

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }


    @Test
    void uploadFile() throws IOException {
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

        String value = "Successful";
        ResponseEntity<ResponseMessage> message = ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage(value));

        Mockito.when(fileStorageService.store(file,id)).thenReturn(fileDB);

        ResponseEntity<GenericMessage> newMessage = fileController.uploadFile(file,id);

        assertThat(newMessage).isNotNull();
        assertEquals(newMessage.getStatusCode(),message.getStatusCode());
    }

    @Test
    void throwErrorIfFileNotStoredInDb() throws IOException {
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

        String value = "failure";
        ResponseEntity<ResponseMessage> message = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage(value));

        Mockito.when(fileStorageService.store(file,id)).thenReturn(null);

        ResponseEntity<GenericMessage> newMessage = fileController.uploadFile(file,id);

        assertThat(newMessage).isNotNull();
        assertEquals(newMessage.getStatusCode(),message.getStatusCode());
    }

    @Test
    void throwsException() throws IOException {
        final Long id = 1L;

        MultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        Mockito.when(fileStorageService.store(file,id)).thenThrow(IOException.class);

        ResponseEntity<GenericMessage> response = fileController.uploadFile(file,id);
        assertEquals(HttpStatus.EXPECTATION_FAILED,response.getStatusCode());
    }


    @Test
    void testGetFileById() {
        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(null);
        fileDB.setId(1L);
        fileDB.setType(".png");
        fileDB.setName("file1");
        fileDB.setAppointmentId(id);

        Mockito.when(fileStorageService.getFile(id)).thenReturn(fileDB);

        ResponseEntity<byte[]> newFile = fileController.getFile(id);
        System.out.println(newFile.getStatusCodeValue());

        assertThat(newFile).isNotNull();
        assertEquals(200,newFile.getStatusCodeValue());

    }

    @Test
    void throwExceptionWhenIdNotPresentInDbForReport() {
        final Long id = 1L;

        Mockito.when(fileStorageService.getFile(id)).thenReturn(null);

        assertThrows(ResourceNotFound.class,() -> {
            fileController.getFile(id);
        });
    }
}