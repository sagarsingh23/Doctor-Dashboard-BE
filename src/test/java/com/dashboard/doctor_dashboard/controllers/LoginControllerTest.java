package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.JwtToken;
import com.dashboard.doctor_dashboard.services.LoginService;
import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
        Mockito.when(loginService.tokenVerification(idToken.getIdtoken())).thenReturn(new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,token),HttpStatus.CREATED));

        String content = objectMapper.writeValueAsString(idToken);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user/login").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated());


    }


    @Test
    @DisplayName("Server Status Check")
    void checkServerStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/check").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    @DisplayName("Delete User Details")
    void deleteDoctorById() throws Exception {
         final Long id = 1L;

        String message = "Successfully deleted";

        Mockito.when(loginService.deleteDoctorById(Mockito.any(Long.class))).thenReturn(message);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/private/user/login/delete/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }
}