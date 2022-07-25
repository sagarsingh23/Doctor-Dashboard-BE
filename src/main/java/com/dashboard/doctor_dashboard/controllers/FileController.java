package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.services.file_service.FileStorageService;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RequestMapping("api/v1/files/")
public class FileController {


    private FileStorageService storageService;

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
    @PostMapping("patient/{id}/upload")
    public ResponseEntity<GenericMessage> uploadFile(@RequestParam MultipartFile file, @PathVariable("id") Long id) throws IOException {
        log.info("FileController::uploadFile");
        return storageService.store(file,id);
    }


    /**
     * @param id  is used as path variable
     * @return download file
     */
    @ApiOperation("This API is responsible for downloading of file")
    @GetMapping("{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id){
        log.info("FileController::getFile");
        return storageService.getFile(id);
    }
}