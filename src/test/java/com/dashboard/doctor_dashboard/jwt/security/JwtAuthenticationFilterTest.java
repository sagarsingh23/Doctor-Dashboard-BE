package com.dashboard.doctor_dashboard.jwt.security;

import com.dashboard.doctor_dashboard.entities.login_entity.LoginDetails;
import com.dashboard.doctor_dashboard.jwt.security.CustomUserDetailsService;
import com.dashboard.doctor_dashboard.jwt.security.JwtAuthenticationFilter;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.utils.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter authenticationFilter;

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
    void doFilterInternalTest() throws ServletException, IOException {
        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqaWdtZXQucmluY2hlbkBuaW5lbGVhcHMuY29tIiwiRG9jdG9yRGV0YWlscyI6eyJkb2N0b3JJZCI6NCwiZG9jdG9yTmFtZSI6ImppZ21ldCIsImRvY3RvckVtYWlsIjoiamlnbWV0LnJpbmNoZW5AbmluZWxlYXBzLmNvbSIsInJvbGUiOiJET0NUT1IiLCJwcm9maWxlUGljIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EtL0FGZFp1Y3F1dUExT0FMQUZpVlRIMldxTV9mQ29LR0UzZmlGbk5RSUl1OEE9czk2LWMifSwicm9sZSI6IkRPQ1RPUiIsImV4cCI6MTY1NzQzMTQ5OCwiaWF0IjoxNjU3MzQ1MDk4fQ.5EsBF7HKfTkcpbOTK5ks1IClSHvo0swO8R6cvdv-40q85UgOs1YSIvn9R_iQtlNBlCGMUSLCq96XOOLA-f7Jag";
        HttpServletRequest request = mock(HttpServletRequest.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();

        Mockito.when(request.getHeader("Authorization")).thenReturn(token);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        LoginDetails loginDetails=new LoginDetails(1L,"Pranay","pranay@gmail.com","nineleaps","profilePic1","Doctor",false,null,null,null);

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                String roles = loginDetails.getRole();
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(roles));
                return authorities;
            }

            @Override
            public String getPassword() {
                return loginDetails.getName();
            }

            @Override
            public String getUsername() {
                return loginDetails.getEmailId();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };


        Mockito.when(tokenProvider.validateToken(Mockito.any(String.class))).thenReturn(true);
        Mockito.when(tokenProvider.getUsernameFromJWT(Mockito.any(String.class))).thenReturn("jigmet");
        Mockito.when(customUserDetailsService.loadUserByUsername(Mockito.any(String.class))).thenReturn(userDetails);

        authenticationFilter.doFilterInternal(request,response,filterChain);
        authenticationFilter.doFilterInternal(request,response,filterChain);

        verify(filterChain,times(2)).doFilter(request,response);
        assertEquals("success",authenticationFilter.status);
    }

    @Test
    void returnNullIfTokenIsNotValid() throws ServletException, IOException {

        String token = "Bear eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqaWdtZXQucmluY2hlbkBuaW5lbGVhcHMuY29tIiwiRG9jdG9yRGV0YWlscyI6eyJkb2N0b3JJZCI6NCwiZG9jdG9yTmFtZSI6ImppZ21ldCIsImRvY3RvckVtYWlsIjoiamlnbWV0LnJpbmNoZW5AbmluZWxlYXBzLmNvbSIsInJvbGUiOiJET0NUT1IiLCJwcm9maWxlUGljIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EtL0FGZFp1Y3F1dUExT0FMQUZpVlRIMldxTV9mQ29LR0UzZmlGbk5RSUl1OEE9czk2LWMifSwicm9sZSI6IkRPQ1RPUiIsImV4cCI6MTY1NzQzMTQ5OCwiaWF0IjoxNjU3MzQ1MDk4fQ.5EsBF7HKfTkcpbOTK5ks1IClSHvo0swO8R6cvdv-40q85UgOs1YSIvn9R_iQtlNBlCGMUSLCq96XOOLA-f7Jag";
        HttpServletRequest request = mock(HttpServletRequest.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn(token);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        authenticationFilter.doFilterInternal(request,response,filterChain);

        assertEquals(Constants.FAIL,authenticationFilter.status);

    }

    @Test
    void returnNullIfBearerTokenIsEmpty() throws ServletException, IOException {

        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqaWdtZXQucmluY2hlbkBuaW5lbGVhcHMuY29tIiwiRG9jdG9yRGV0YWlscyI6eyJkb2N0b3JJZCI6NCwiZG9jdG9yTmFtZSI6ImppZ21ldCIsImRvY3RvckVtYWlsIjoiamlnbWV0LnJpbmNoZW5AbmluZWxlYXBzLmNvbSIsInJvbGUiOiJET0NUT1IiLCJwcm9maWxlUGljIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EtL0FGZFp1Y3F1dUExT0FMQUZpVlRIMldxTV9mQ29LR0UzZmlGbk5RSUl1OEE9czk2LWMifSwicm9sZSI6IkRPQ1RPUiIsImV4cCI6MTY1NzQzMTQ5OCwiaWF0IjoxNjU3MzQ1MDk4fQ.5EsBF7HKfTkcpbOTK5ks1IClSHvo0swO8R6cvdv-40q85UgOs1YSIvn9R_iQtlNBlCGMUSLCq96XOOLA-f7Jag";
        HttpServletRequest request = mock(HttpServletRequest.class);

        Mockito.when(request.getHeader("Authorize")).thenReturn(token);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        authenticationFilter.doFilterInternal(request,response,filterChain);

        assertEquals(Constants.FAIL,authenticationFilter.status);

    }

    @Test
    void returnFailureIfValidateTokenIsFalse() throws ServletException, IOException {

        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqaWdtZXQucmluY2hlbkBuaW5lbGVhcHMuY29tIiwiRG9jdG9yRGV0YWlscyI6eyJkb2N0b3JJZCI6NCwiZG9jdG9yTmFtZSI6ImppZ21ldCIsImRvY3RvckVtYWlsIjoiamlnbWV0LnJpbmNoZW5AbmluZWxlYXBzLmNvbSIsInJvbGUiOiJET0NUT1IiLCJwcm9maWxlUGljIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EtL0FGZFp1Y3F1dUExT0FMQUZpVlRIMldxTV9mQ29LR0UzZmlGbk5RSUl1OEE9czk2LWMifSwicm9sZSI6IkRPQ1RPUiIsImV4cCI6MTY1NzQzMTQ5OCwiaWF0IjoxNjU3MzQ1MDk4fQ.5EsBF7HKfTkcpbOTK5ks1IClSHvo0swO8R6cvdv-40q85UgOs1YSIvn9R_iQtlNBlCGMUSLCq96XOOLA-f7Jag";
        HttpServletRequest request = mock(HttpServletRequest.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn(token);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        Mockito.when(tokenProvider.validateToken(Mockito.any(String.class))).thenReturn(false);

        authenticationFilter.doFilterInternal(request,response,filterChain);

        assertEquals(Constants.FAIL,authenticationFilter.status);

    }
}