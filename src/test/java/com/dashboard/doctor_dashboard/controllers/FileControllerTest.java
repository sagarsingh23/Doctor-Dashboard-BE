package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.Utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.entities.report.FileDB;
import com.dashboard.doctor_dashboard.entities.report.ResponseMessage;
import com.dashboard.doctor_dashboard.services.patient_service.impl.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private FileController fileController;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();

        System.out.println("setting up");
    }



    @Test
    void uploadFile() throws Exception {


        MockMultipartFile file = new MockMultipartFile(
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

        String value = "Successful";
        ResponseEntity<ResponseMessage> message = ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage(value));

        Mockito.when(fileStorageService.store(Mockito.any(MultipartFile.class),Mockito.any(Long.class))).thenReturn(fileDB);

        String content = objectMapper.writeValueAsString(file.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/patient/upload/1")
                        .file(file).contentType(MediaType.MULTIPART_FORM_DATA)
                .content(content)).andExpect(status().isCreated());

    }

    @Test
    void throwErrorIfFileNotStoredInDb() throws Exception {
        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(null);
        fileDB.setId(id);
        fileDB.setType(".png");
        fileDB.setName("file1");
        fileDB.setAppointmentId(id);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        String value = "Successful";
        ResponseEntity<ResponseMessage> message = ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage(value));

        String content = objectMapper.writeValueAsString(file.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/patient/upload/1")
                .file(file).contentType(MediaType.MULTIPART_FORM_DATA)
                .content(content)).andExpect(status().isBadRequest());
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
    void testGetFileById() throws Exception {
        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(null);
        fileDB.setId(1L);
        fileDB.setType(".png");
        fileDB.setName("file1");
        fileDB.setAppointmentId(id);

        Mockito.when(fileStorageService.getFile(id)).thenReturn(fileDB);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/files/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    void throwExceptionWhenIdNotPresentInDbForReport() throws Exception {
        final Long id = 1L;

        Mockito.when(fileStorageService.getFile(id)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/files/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

    }
}