package com.dashboard.doctor_dashboard.services.prescription_service;

import com.dashboard.doctor_dashboard.entities.Prescription;
import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entities.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFound;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.repository.PrescriptionRepository;
import com.dashboard.doctor_dashboard.services.PdFGeneratorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService   {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private PdFGeneratorServiceImpl pdFGeneratorService;



    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private JavaMailSender mailSender;




    @Override
    public String addPrescription(Long appointId, UpdatePrescriptionDto updatePrescriptionDto) throws IOException, MessagingException, JSONException {

           if (appointmentRepository.getId(appointId) != null) {
               if(appointmentRepository.checkStatus(appointId).equals("Vitals updated")){
                    if (appointId.equals(updatePrescriptionDto.getPrescriptions().get(0).getAppointment().getAppointId())) {

                      appointmentRepository.changeAppointmentStatus(appointId, updatePrescriptionDto.getStatus());
                      attributeRepository.changeNotes(appointId, updatePrescriptionDto.getNotes());
                      prescriptionRepository.saveAll(updatePrescriptionDto.getPrescriptions());
                      pdFGeneratorService.generatePdf(updatePrescriptionDto.getPrescriptions(), updatePrescriptionDto.getPatientDto(), updatePrescriptionDto.getNotes());
                      sendEmailToUserAfterPrescription(updatePrescriptionDto.getPatientDto());
                      log.debug(Constants.PRESCRIPTION_CREATED);
                      return Constants.PRESCRIPTION_CREATED;
                    }
               throw new ResourceNotFoundException(Constants.APPOINTMENT, "id", updatePrescriptionDto.getPrescriptions().get(0).getAppointment().getAppointId());
              }
               else
                   throw new APIException(HttpStatus.BAD_REQUEST,"Prescription cannot be added for other status like completed,follow Up, and to be attended");
            }
             throw new ResourceNotFoundException(Constants.APPOINTMENT,"id", appointId);
    }

    @Override
    public List<Prescription> getAllPrescriptionByAppointment(Long appointId) {
        if(appointmentRepository.getId(appointId) != null){
            return prescriptionRepository.getAllPrescriptionByAppointment(appointId);
        }
        throw new ResourceNotFoundException(Constants.APPOINTMENT,"id",appointId);

    }

    @Override
    public ResponseEntity<GenericMessage> deleteAppointmentById(Long id) {
        var genericMessage = new GenericMessage();
        prescriptionRepository.deleteById(id);
        genericMessage.setData("successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage, HttpStatus.OK);
    }


    public void sendEmailToUserAfterPrescription(PatientDto patientDto) throws JSONException, MessagingException, UnsupportedEncodingException {
        log.info("Prescription Mail Service Started");
        String toEmail = patientDto.getPatientEmail();
        var fromEmail = "mecareapplication@gmail.com";
        var senderName = "meCare Application";
        var subject = "Prescription Updated";

        String content = Constants.MAIL_PRESCRIPTION;

        content = content.replace("[[name]]", patientDto.getPatientName());
        content = content.replace("[[doctorName]]", patientDto.getDoctorName());

        var obj = new JSONObject();
        obj.put("fromEmail", fromEmail);
        obj.put("toEmail", toEmail);
        obj.put("senderName", senderName);
        obj.put("subject", subject);
        obj.put("content", content);
        sendMailer(obj);
    }


    private void sendMailer(JSONObject obj) throws MessagingException, JSONException, UnsupportedEncodingException {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message,true);
            helper.setFrom(obj.get("fromEmail").toString(), obj.get("senderName").toString());
            helper.setTo(obj.get("toEmail").toString());
            helper.setText(obj.get("content").toString(), true);
            helper.setSubject(obj.get("subject").toString());
            var fileSystemResource=new FileSystemResource("/home/nineleaps/Downloads/prescription/prescription.pdf");
            helper.addAttachment(fileSystemResource.getFilename(),fileSystemResource);
            mailSender.send(message);
            log.info("Mail Sent...\n stopping mail service");


        }catch (Exception e)
        {
            log.info("Exception!!! Mail service stopped..");
            throw new ResourceNotFound(e.getMessage());
        }

    }


}
