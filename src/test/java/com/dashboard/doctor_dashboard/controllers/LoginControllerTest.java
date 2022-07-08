package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.login_entity.JwtToken;
import com.dashboard.doctor_dashboard.exceptions.GoogleLoginException;
import com.dashboard.doctor_dashboard.services.login_service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();

        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }

    @Test
    void loginJwtToken() throws Exception {
        String token = "abcdefghijklmnopqrstuvwxyz";
        JwtToken idToken = new JwtToken();
        idToken.setIdtoken(token);
        Mockito.when(loginService.tokenVerification(idToken.getIdtoken())).thenReturn(token);

        String content = objectMapper.writeValueAsString(idToken);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user/login").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated());


    }

    @Test
    void ThrowErrorIfTokenExpired() throws Exception {
        String token = "ID token expired.";
        JwtToken idToken = new JwtToken();
        idToken.setIdtoken(token);
        Mockito.when(loginService.tokenVerification(idToken.getIdtoken())).thenReturn(token);

        String content = objectMapper.writeValueAsString(idToken);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user/login").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isUnauthorized());


    }


    @Test
    void checkServerStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/check").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void deleteDoctorById() throws Exception {
         final Long id = 1L;

        String message = "Successfully deleted";

        Mockito.when(loginService.deleteDoctorById(Mockito.any(Long.class))).thenReturn(message);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/doctor/login/delete/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }
}