package com.dashboard.doctor_dashboard.service;

import com.dashboard.doctor_dashboard.jwt.entity.DoctorClaims;
import com.dashboard.doctor_dashboard.jwt.entity.Login;
import com.dashboard.doctor_dashboard.jwt.service.JwtServiceImpl;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JwtServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private JwtServiceImpl jwtService;

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
    void authenticateUser() {
        String token = "ejsdetyfrbendsntsqodqpepmmgrewfetesdy";

        Login login = new Login();
        login.setEmail("sagarssn23@gmail.com");
        login.setId(1L);
        login.setUsername("sagar24");

        DoctorClaims doctorClaims = new DoctorClaims();
        doctorClaims.setDoctorId(login.getId());
        doctorClaims.setDoctorEmail(login.getEmail());
        doctorClaims.setDoctorName(login.getUsername());

        Authentication authentication = mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.getEmail(),login.getUsername()
        ))).thenReturn(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);



        Mockito.when(jwtTokenProvider.generateToken(authentication,doctorClaims)).thenReturn(token);

        String newToken = jwtService.authenticateUser(login);

        assertEquals(token,newToken);
        assertEquals(authentication,SecurityContextHolder.getContext().getAuthentication());
    }
}