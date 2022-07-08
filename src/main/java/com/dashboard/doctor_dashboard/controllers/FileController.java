package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.Utils.Constants;
import com.dashboard.doctor_dashboard.Utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.entities.report.ResponseMessage;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.services.patient_service.impl.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

    @Autowired
    private FileStorageService storageService;


    @ResponseBody
    @PostMapping("/api/v1/patient/upload/{id}")
    public ResponseEntity<GenericMessage> uploadFile(@RequestParam MultipartFile file, @PathVariable("id") Long id) {
        var genericMessage = new GenericMessage();

        var message = "";
        try {
            var fileDB = storageService.store(file, id);
            if (fileDB == null) {
                genericMessage.setData(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Patient ID not Found:" + id)));
                genericMessage.setStatus(Constants.FAIL);
                return new ResponseEntity<>(genericMessage,HttpStatus.BAD_REQUEST);
            }
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
             genericMessage.setData(ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message)));
             genericMessage.setStatus(Constants.SUCCESS);
             return new ResponseEntity<>(genericMessage,HttpStatus.CREATED);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            genericMessage.setData(ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message)));
            genericMessage.setStatus(Constants.FAIL);
            return new ResponseEntity<>(genericMessage,HttpStatus.EXPECTATION_FAILED);
        }
    }


    @GetMapping("/v1/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            var fileDB = storageService.getFile(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                    .body(fileDB.getDataReport());
        } catch (Exception e) {
            throw new ResourceNotFoundException("No Report Found!!!");
        }

    }
}