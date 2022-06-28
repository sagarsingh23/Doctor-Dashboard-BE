package com.dashboard.doctor_dashboard.services.prescription_service;

import com.dashboard.doctor_dashboard.entities.Prescription;
import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entities.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ReportNotFound;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.repository.PrescriptionRepository;
import com.dashboard.doctor_dashboard.services.PdFGeneratorServiceImpl;
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
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
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
                    if (appointId == updatePrescriptionDto.getPrescriptions().get(0).getAppointment().getAppointId()) {

                      appointmentRepository.changeAppointmentStatus(appointId, updatePrescriptionDto.getStatus());
                      attributeRepository.changeNotes(appointId, updatePrescriptionDto.getNotes());
                      prescriptionRepository.saveAll(updatePrescriptionDto.getPrescriptions());
                      pdFGeneratorService.generatePdf(updatePrescriptionDto.getPrescriptions(), updatePrescriptionDto.getPatientDto(), updatePrescriptionDto.getNotes());
                      sendEmailToUserAfterPrescription(updatePrescriptionDto.getPatientDto());

                      return "Prescription Added";
                    }
               throw new ResourceNotFoundException("Appointment", "id", updatePrescriptionDto.getPrescriptions().get(0).getAppointment().getAppointId());
              }
               else
                   throw new APIException(HttpStatus.BAD_REQUEST,"Prescription cannot be added for other status like completed,follow Up, and to be attended");
            }
             throw new ResourceNotFoundException("Appointment", "id", appointId);
    }

    @Override
    public List<Prescription> getAllPrescriptionByAppointment(Long appointId) {
        if(appointmentRepository.getId(appointId) != null){
            return prescriptionRepository.getAllPrescriptionByAppointment(appointId);
        }
        throw new ResourceNotFoundException("Appointment","id",appointId);

    }

    @Override
    public ResponseEntity<GenericMessage> deleteAppointmentById(Long id) {
        GenericMessage genericMessage = new GenericMessage();
        prescriptionRepository.deleteById(id);
        genericMessage.setData("successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage, HttpStatus.OK);
    }


    public void sendEmailToUserAfterPrescription(PatientDto patientDto) throws JSONException, MessagingException, UnsupportedEncodingException {
        String toEmail = patientDto.getPatientEmail();
        String fromEmail = "mecareapplication@gmail.com";
        String senderName = "meCare Application";
        String subject = "Prescription Updated";

        String content = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
                + "</style></head>"
                + "<div style=\"background-color: white; color:black  \">\n"
                + " <p style=\"text-align: left; font-size:15px ;\">Hi [[name]],</p>\n"
                + " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
                + " font-family: 'Arial' \n" + " ;\n"
                + " \"\n" + " >\n"
                + "Dr. [[doctorName]] Completed Your Appointment. Check the prescription given below in the attachment.</p>"
                + " <p style=\" text-align: left ;font-size:13px \">\n"
                + " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
                + " >mecareapplication@gmail.com</span\n" + " >\n" + " </p>\n"
                + " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
                + " Thanks & Regards, </p>\n"
                + " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">meCare Application team</span\n"
                + " </div>";

        content = content.replace("[[name]]", patientDto.getPatientName());
        content = content.replace("[[doctorName]]", patientDto.getDoctorName());

        JSONObject obj = new JSONObject();
        obj.put("fromEmail", fromEmail);
        obj.put("toEmail", toEmail);
        obj.put("senderName", senderName);
        obj.put("subject", subject);
        obj.put("content", content);
        sendMailer(obj);
    }


    private void sendMailer(JSONObject obj) throws MessagingException, JSONException, UnsupportedEncodingException {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(obj.get("fromEmail").toString(), obj.get("senderName").toString());
            helper.setTo(obj.get("toEmail").toString());
            helper.setText(obj.get("content").toString(), true);
            helper.setSubject(obj.get("subject").toString());
            System.out.println("I am printing"+message.getSubject());
            FileSystemResource fileSystemResource=new FileSystemResource("/home/nineleaps/Downloads/prescription/prescription.pdf");
            helper.addAttachment(fileSystemResource.getFilename(),fileSystemResource);


            System.out.println("hii");
            mailSender.send(message);
        }catch (Exception e)
        {
            throw new ReportNotFound(e.getMessage());
        }

    }


}
