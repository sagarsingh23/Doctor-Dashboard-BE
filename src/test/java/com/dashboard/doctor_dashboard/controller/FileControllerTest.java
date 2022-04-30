package com.dashboard.doctor_dashboard.controller;

import com.dashboard.doctor_dashboard.entity.report.FileDB;
import com.dashboard.doctor_dashboard.entity.report.ResponseMessage;
import com.dashboard.doctor_dashboard.service.patient_service.impl.FileStorageService;
import com.dashboard.doctor_dashboard.exception.ReportNotFound;
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
        fileDB.setPatientId(id);

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

        ResponseEntity<ResponseMessage> newMessage = fileController.uploadFile(file,id);

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
        fileDB.setPatientId(id);

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

        ResponseEntity<ResponseMessage> newMessage = fileController.uploadFile(file,id);

        assertThat(newMessage).isNotNull();
        assertEquals(newMessage.getStatusCode(),message.getStatusCode());
    }



//    @Test
//    @Disabled
//    void getListFiles() {
//
//        List<ResponseFile> files = new ArrayList<>();
//        ResponseFile file1 = new ResponseFile("file1",null,".png",10000);
//        ResponseFile file2 = new ResponseFile("file2",null,".png",10000);
//        files.addAll(Arrays.asList(file1,file2));
//
//
//        List<FileDB> report = new ArrayList<>();
//        final Long id = 1L;
//        FileDB fileDB = new FileDB();
//        fileDB.setDataReport(null);
//        fileDB.setId(1L);
//        fileDB.setType(".png");
//        fileDB.setName("file1");
//        fileDB.setPatientId(id);
//
//        FileDB fileDB1 = new FileDB();
//        fileDB1.setDataReport(null);
//        fileDB1.setId(2L);
//        fileDB1.setType(".png");
//        fileDB1.setName("file2");
//        fileDB1.setPatientId(id);
//
//        Mockito.when(fileStorageService.getAllFiles().map(dbFile -> {
//            ServletUriComponentsBuilder newValue = Mockito.any(ServletUriComponentsBuilder.class);
//            return new ResponseFile(
//                    dbFile.getName(),
//                    newValue.toString(),
//                    dbFile.getType(),
//                    dbFile.getDataReport().length);
//        }).collect(Collectors.toList())).thenReturn(files.stream().toList());
//
//        ResponseEntity<List<ResponseFile>> newFile = fileController.getListFiles();
//        System.out.println(newFile);
//
//
//
//
//
//    }


    @Test
    void testGetFileById() {
        final Long id = 1L;
        FileDB fileDB = new FileDB();
        fileDB.setDataReport(null);
        fileDB.setId(1L);
        fileDB.setType(".png");
        fileDB.setName("file1");
        fileDB.setPatientId(id);

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

        assertThrows(ReportNotFound.class,() -> {
            fileController.getFile(id);
        });
    }
}