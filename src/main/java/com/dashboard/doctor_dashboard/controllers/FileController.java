package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.services.patient_service.impl.FileStorageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {


    private  FileStorageService storageService;

    @Autowired
    public FileController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    /** This controller is for handling the API call to upload a file.
     * @param file is used as request param.
     * @param id is used as path variable.
     * @return uploaded file.
     * @throws ResourceNotFoundException throws Exception
     */
    @ApiOperation("This controller is for handling the API call to upload a file")
    @ResponseBody
    @PostMapping("/api/v1/patient/upload/{id}")
    public ResponseEntity<GenericMessage> uploadFile(@RequestParam MultipartFile file, @PathVariable("id") Long id) throws IOException {
        return storageService.store(file,id);
    }


    /**
     * @param id  is used as path variable
     * @return download file
     */
    @ApiOperation("This API is responsible for downloading of file")
    @GetMapping("/v1/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id){
       return storageService.getFile(id);
    }
}