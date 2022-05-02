package com.dashboard.doctor_dashboard.service.patient_service.impl;

import com.dashboard.doctor_dashboard.entity.report.FileDB;
import com.dashboard.doctor_dashboard.repository.FileDBRepository;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Autowired
    private FileDBRepository fileDBRepository;

    @Autowired
    private PatientRepository patientRepository;

    public FileDB store(MultipartFile file, Long id) throws IOException {

        Long temp = patientRepository.getId(id);
        if (temp == null) {
            return null;
        }
        String value = file.getOriginalFilename();
        if(value != null) {
            var fileName = StringUtils.cleanPath(value);
            var fileDB = new FileDB(fileName, file.getContentType(), file.getBytes(), id);
            return fileDBRepository.save(fileDB);
        }
        return null;
    }


    public FileDB getFile(Long id) {
        return fileDBRepository.findByPatientId(id);
    }


    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

}