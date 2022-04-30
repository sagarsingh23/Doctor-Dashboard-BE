package com.dashboard.doctor_dashboard.controller;

import com.dashboard.doctor_dashboard.entity.login_entity.IdToken;
import com.dashboard.doctor_dashboard.exception.GoogleLoginException;
import com.dashboard.doctor_dashboard.service.login_service.LoginService;
import org.codehaus.jettison.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
    void loginIdToken() throws GeneralSecurityException, IOException, JSONException {
        String token = "abcdefghijklmnopqrstuvwxyz";
        IdToken idToken = new IdToken();
        idToken.setIdtoken(token);
        Mockito.when(loginService.tokenVerification(idToken.getIdtoken())).thenReturn(token);

        ResponseEntity<String> newMessage = loginController.loginIdToken(idToken);
        System.out.println(newMessage.getBody().getClass());
        assertThat(newMessage).isNotNull();
        assertEquals(200,newMessage.getStatusCodeValue());;
        assertEquals(true,newMessage.hasBody());

    }

    @Test
    void ThrowErrorIfTokenExpired() throws GeneralSecurityException, IOException, JSONException {
        String token = "ID token expired.";
        IdToken idToken = new IdToken();
        idToken.setIdtoken(token);
        Mockito.when(loginService.tokenVerification(idToken.getIdtoken())).thenReturn(token);

        assertThrows(GoogleLoginException.class, ()->{
            loginController.loginIdToken(idToken);
        });

    }


}