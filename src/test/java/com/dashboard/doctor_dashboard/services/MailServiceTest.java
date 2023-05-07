package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.services.impl.MailServiceImpl;
import org.codehaus.jettison.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MailServiceTest {

    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private ITemplateEngine templateEngine;


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }


    @Test
    void mailServiceHandler_SUCCESS() throws MessagingException, JSONException, UnsupportedEncodingException {
        Mockito.doNothing().when(mailSender).send((MimeMessage) any());
        Mockito.doReturn(mimeMessage).when(mailSender).createMimeMessage();
        Mockito.doReturn("text").when(templateEngine).process(Mockito.any(String.class),Mockito.any(Context.class));
        mailService.mailServiceHandler("xyz@gmail.com","xyz@gmail.com","sagar","Prescription" ,"templatePrescription", new Context());
        mailService.mailServiceHandler("xyz@gmail.com","xyz@gmail.com","sagar","Prescription" ,"templatePrescription", new Context());

        verify(mailSender,times(2)).send((MimeMessage) any());

    }

//    @Test
//    void mailServiceHandler_FAILURE() throws MessagingException, JSONException, UnsupportedEncodingException {
//
//        MailErrorException mailErrorException = assertThrows(MailErrorException.class,()->{
//            mailService.mailServiceHandler("xyz@gmail.com","xyz@gmail.com","sagar","Prescription" ,"templatePrescription",new Context());
//
//        });
//        assertEquals(Constants.MAIL_ERROR,mailErrorException.getMessage());
//
//    }
}