package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.jwt.entities.Claims;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

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
        login.setEmail("sagar.singh@nineleaps.com");
        login.setId(1L);
        login.setUsername("sagar24");
        login.setRole("DOCTOR");
        login.setProfilePic("profilePic");

        Claims claims = new Claims();
        claims.setDoctorId(login.getId());
        claims.setDoctorEmail(login.getEmail());
        claims.setDoctorName(login.getUsername());
        claims.setRole(login.getRole());
        claims.setProfilePic(login.getProfilePic());



        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(login.getRole()));
        System.out.println(authorities);

        Authentication authentication = mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.getEmail(),login.getUsername(),authorities
        ))).thenReturn(authentication);
        System.out.println(authentication.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);


        Mockito.when(jwtTokenProvider.generateToken(Mockito.any(Authentication.class),Mockito.any(Claims.class))).thenReturn(token);

        String newToken = jwtService.authenticateUser(login);
        System.out.println(newToken);

        assertEquals(token,newToken);
        assertEquals(authentication,SecurityContextHolder.getContext().getAuthentication());
    }
}