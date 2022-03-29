package com.dashboard.doctor_dashboard.Controller;

import com.dashboard.doctor_dashboard.Entity.report.FileDB;
import com.dashboard.doctor_dashboard.Entity.report.ResponseFile;
import com.dashboard.doctor_dashboard.Entity.report.ResponseMessage;
import com.dashboard.doctor_dashboard.Service.patient_service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

    @Autowired
    private FileStorageService storageService;


    @ResponseBody
    @RequestMapping(value = "/api/patient/upload/{id}", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam MultipartFile file,@PathVariable("id") Long id) {
        String message = "";
        try {
            FileDB fileDB=storageService.store(file,id);
            if(fileDB == null)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Patient ID not Found:"+id));
            }
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(String.valueOf(dbFile.getPatientId()))
                    .toUriString();
            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getDataReport().length);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        FileDB fileDB = storageService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getDataReport());
    }
}