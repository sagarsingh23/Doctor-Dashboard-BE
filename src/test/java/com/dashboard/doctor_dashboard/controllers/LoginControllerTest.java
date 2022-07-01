package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.login_entity.JwtToken;
import com.dashboard.doctor_dashboard.exceptions.GoogleLoginException;
import com.dashboard.doctor_dashboard.services.login_service.LoginService;
import org.codehaus.jettison.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }

    @Test
    void loginJwtToken() throws GeneralSecurityException, IOException, JSONException {
        String token = "abcdefghijklmnopqrstuvwxyz";
        JwtToken idToken = new JwtToken();
        idToken.setIdtoken(token);
        Mockito.when(loginService.tokenVerification(idToken.getIdtoken())).thenReturn(token);

        ResponseEntity<String> newMessage = loginController.tokenAuthentication(idToken);
        System.out.println(newMessage.getBody().getClass());
        assertThat(newMessage).isNotNull();
        assertEquals(200,newMessage.getStatusCodeValue());;
        assertEquals(true,newMessage.hasBody());

    }

    @Test
    void ThrowErrorIfTokenExpired() throws GeneralSecurityException, IOException, JSONException {
        String token = "ID token expired.";
        JwtToken idToken = new JwtToken();
        idToken.setIdtoken(token);
        Mockito.when(loginService.tokenVerification(idToken.getIdtoken())).thenReturn(token);

        assertThrows(GoogleLoginException.class, ()->{
            loginController.tokenAuthentication(idToken);
        });

    }


    @Test
    void checkServerStatus() {
        ResponseEntity<String> response = loginController.checkServerStatus();
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void deleteDoctorById() {
         final Long id = 1L;

        String message = "Successfully deleted";

        Mockito.when(loginService.deleteDoctorById(Mockito.any(Long.class))).thenReturn(message);

        String newMessage = loginController.deleteDoctorById(id);
        assertThat(newMessage).isNotNull();
        assertEquals(message,newMessage);
    }
}