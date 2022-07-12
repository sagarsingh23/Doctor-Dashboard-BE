package com.dashboard.doctor_dashboard.jwt.security;

import com.dashboard.doctor_dashboard.jwt.security.JwtAuthenticationEntryPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtAuthenticationEntryPointTest {

    @InjectMocks
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }


    @Test
    void commence() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authenticationException = new AuthenticationException("Authentication Exception") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };

        jwtAuthenticationEntryPoint.commence(request,response,authenticationException);
        jwtAuthenticationEntryPoint.commence(request,response,authenticationException);


        verify(response,times(2)).sendError(HttpServletResponse.SC_UNAUTHORIZED,authenticationException.getMessage());
    }

}