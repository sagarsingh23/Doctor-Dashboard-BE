package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.exceptions.MailErrorException;
import org.codehaus.jettison.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MailServiceTest {

    @InjectMocks
    private MailService mailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }


    @Test
    void mailServiceHandler_SUCCESS() throws MessagingException, JSONException, UnsupportedEncodingException {
        Mockito.doNothing().when(mailSender).send((MimeMessage) any());
        Mockito.doReturn(mimeMessage).when(mailSender).createMimeMessage();

        mailService.mailServiceHandler("sagarssn23@gmail.com","sagarssn23@gmail.com","sagar","Prescription" ,Constants.MAIL_PRESCRIPTION);
        mailService.mailServiceHandler("sagarssn23@gmail.com","sagarssn23@gmail.com","sagar","Prescription" ,Constants.MAIL_PRESCRIPTION);

        verify(mailSender,times(2)).send((MimeMessage) any());

    }

    @Test
    void mailServiceHandler_FAILURE() throws MessagingException, JSONException, UnsupportedEncodingException {

        MailErrorException mailErrorException = assertThrows(MailErrorException.class,()->{
            mailService.mailServiceHandler("sagarssn23@gmail.com","sagarssn23@gmail.com","sagar","Prescription" ,Constants.MAIL_PRESCRIPTION);

        });
        assertEquals(Constants.MAIL_ERROR,mailErrorException.getMessage());

    }
}