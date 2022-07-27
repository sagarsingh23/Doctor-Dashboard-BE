package com.dashboard.doctor_dashboard.services.impl;

import com.dashboard.doctor_dashboard.entities.FileDB;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.FileDBRepository;
import com.dashboard.doctor_dashboard.services.FileStorageService;
import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * FileStorageServiceImpl
 */
@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final FileDBRepository fileDBRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public FileStorageServiceImpl(FileDBRepository fileDBRepository, AppointmentRepository appointmentRepository) {
        this.fileDBRepository = fileDBRepository;
        this.appointmentRepository = appointmentRepository;
    }


    /**
     * This function of service is for uploading files.
     * @param file
     * @param id
     * @return ResponseEntity<GenericMessage> with status code 201.
     */
    public ResponseEntity<GenericMessage> store(MultipartFile file, Long id) throws IOException {
        log.info("inside: FileServiceStorageImpl::store");

        var message = "";
        if (appointmentRepository.getId(id) == null) {
            log.info("exit: FileServiceStorageImpl::store - Appointment Not Found:"+Constants.APPOINTMENT_NOT_FOUND);
            throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
        }
        String value = file.getOriginalFilename();
        if(value == null){                                                                         //check if file has name or not
            log.info("exit: FileServiceStorageImpl::store - File Not Present:"+Constants.FILE_NAME_PRESENT);
            throw new ResourceNotFoundException(Constants.FILE_NAME_PRESENT);
        }
        var fileName = StringUtils.cleanPath(value);
        var fileDB = new FileDB(fileName, file.getContentType(), file.getBytes(), id);
        fileDBRepository.save(fileDB);
        message = Constants.FILE_UPLOADED + file.getOriginalFilename();
        log.info("exit: FileServiceStorageImpl::store");
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,message),HttpStatus.CREATED);
    }


    /**
     * This function of service is for downloading file.
     * @param id
     * @return ResponseEntity<byte[]> with status code 200 and Blob object of file.
     */
    public ResponseEntity<byte[]> getFile(Long id) {
        log.info("inside: FileServiceStorageImpl::getFile");
        try {
            var fileDB = fileDBRepository.findByAppointmentId(id);
            log.info("exit: FileServiceStorageImpl::getFile");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                    .body(fileDB.getDataReport());
        } catch (Exception e) {
            log.info("exit: FileServiceStorageImpl::getFile"+Constants.REPORT_NOT_FOUND);
            throw new ResourceNotFoundException(Constants.REPORT_NOT_FOUND);
        }
    }

}