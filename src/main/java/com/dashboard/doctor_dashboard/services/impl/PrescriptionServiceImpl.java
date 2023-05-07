package com.dashboard.doctor_dashboard.services.impl;

import com.dashboard.doctor_dashboard.services.PrescriptionService;
import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.dtos.PatientDto;
import com.dashboard.doctor_dashboard.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.PrescriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * PrescriptionServiceImpl
 */
@Service
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final AttributeRepository attributeRepository;
    private final PdFGeneratorServiceImpl pdFGeneratorService;
    private final MailServiceImpl mailService;

    @Autowired
    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository, AttributeRepository attributeRepository, PdFGeneratorServiceImpl pdFGeneratorService, MailServiceImpl mailService) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.attributeRepository = attributeRepository;
        this.pdFGeneratorService = pdFGeneratorService;
        this.mailService = mailService;
    }

    /**
     * This function of service is for adding prescription of patient.
     * @param appointId
     * @param updatePrescriptionDto which contains patientDto,prescription status,notes etc
     * @return ResponseEntity<GenericMessage> with status code 201.
     * @throws IOException
     * @throws MessagingException
     * @throws JSONException
     */
    @Override
    public ResponseEntity<GenericMessage> addPrescription(Long appointId, UpdatePrescriptionDto updatePrescriptionDto) throws IOException, MessagingException, JSONException {
      log.info("inside: PrescriptionServiceImpl:: addPrescription");
           if (appointmentRepository.getId(appointId) != null) {
               if(appointmentRepository.checkStatus(appointId).equals("Vitals updated")){
                    if (appointId.equals(updatePrescriptionDto.getPrescriptions().get(0).getAppointment().getAppointId())) {

                      appointmentRepository.changeAppointmentStatus(appointId, updatePrescriptionDto.getStatus());
                      attributeRepository.changeNotes(appointId, updatePrescriptionDto.getNotes());
                      prescriptionRepository.saveAll(updatePrescriptionDto.getPrescriptions());
                      pdFGeneratorService.generatePdf(updatePrescriptionDto.getPrescriptions(), updatePrescriptionDto.getPatientDto(), updatePrescriptionDto.getNotes());
                      sendEmailToUserAfterPrescription(updatePrescriptionDto.getPatientDto());
                      log.debug(Constants.PRESCRIPTION_CREATED);
                      log.info("inside: PrescriptionServiceImpl:: addPrescription");
                        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,Constants.PRESCRIPTION_CREATED),HttpStatus.CREATED);
                    }
                   log.info("inside: PrescriptionServiceImpl:: addPrescription inner"+Constants.APPOINTMENT_NOT_FOUND);
                   throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
              }
               else
                   log.info("exit: PrescriptionServiceImpl:: addPrescription -- API exception");
               throw new APIException("Prescription cannot be added for other status like completed,follow Up, and to be attended");
            }
        log.info("exit: PrescriptionServiceImpl:: addPrescription"+Constants.APPOINTMENT_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
    }

    /**
     * This function of service is for getting all prescription of patient by appointment id
     * @param appointId
     * @return ResponseEntity<GenericMessage> with status code 200 and list of prescription
     */
    @Override
    public ResponseEntity<GenericMessage> getAllPrescriptionByAppointment(Long appointId) {
        log.info("inside: PrescriptionServiceImpl:: getAllPrescriptionByAppointment");
        if(appointmentRepository.getId(appointId) != null){
            log.info("exit: PrescriptionServiceImpl:: getAllPrescriptionByAppointment");
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,prescriptionRepository.getAllPrescriptionByAppointment(appointId)),HttpStatus.OK);
        }
        log.info("exit: PrescriptionServiceImpl:: getAllPrescriptionByAppointment"+Constants.APPOINTMENT_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);

    }

    /**
     * This function of service is for deleting appointment by id
     * @param id
     * @return ResponseEntity<GenericMessage> with status code 204 and message successfully deleted.
     */
    @Override
    public ResponseEntity<GenericMessage> deleteAppointmentById(Long id) {
        log.info("inside: PrescriptionServiceImpl:: deleteAppointmentById");

        var genericMessage = new GenericMessage();
        prescriptionRepository.deleteById(id);
        genericMessage.setData("successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        log.info("exit: PrescriptionServiceImpl:: deleteAppointmentById");
        return new ResponseEntity<>(genericMessage, HttpStatus.NO_CONTENT);
    }


    /**
     * This function of service is for sending mail to user after successfully updating his prescription.
     * @param patientDto which contains field category,doctorName,status,patientName etc
     * @throws JSONException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @Value("${spring.mail.username}")
    private String fromEmail;
    public void sendEmailToUserAfterPrescription(PatientDto patientDto) throws JSONException, MessagingException, UnsupportedEncodingException {
        log.info("Prescription Mail Service Started");
        String toEmail = patientDto.getPatientEmail();
        var senderName = "meCare Application";
        var subject = "Prescription Updated";

        var context =  new Context();              // here we are making an object of context and setting up all the values required for mail
        context.setVariable("name", patientDto.getPatientName());
        context.setVariable("doctorName", patientDto.getDoctorName());

        log.info("exit: PrescriptionServiceImpl::sendEmailToUserAfterPrescription");
        mailService.mailServiceHandler(fromEmail,toEmail,senderName,subject,"Prescription",context);
    }


}
