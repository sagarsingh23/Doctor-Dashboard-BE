package com.dashboard.doctor_dashboard.jwt.security;

import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.jwt.entities.Claims;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "encryption");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", 86400000);
    }

    @Test
    void generateTokenAndGetUsernameFromTokenTest_SUCCESS() {
        Login login = new Login();
        login.setEmail("xyz@nineleaps.com");
        login.setId(1L);
        login.setUsername("xyz");
        login.setRole("DOCTOR");
        login.setProfilePic("profilePic");

        Claims claims = new Claims();
        claims.setDoctorId(login.getId());
        claims.setDoctorEmail(login.getEmail());
        claims.setDoctorName(login.getUsername());
        claims.setRole(login.getRole());
        claims.setProfilePic(login.getProfilePic());

        Authentication authentication = mock(Authentication.class);

        Mockito.when(authentication.getName()).thenReturn(claims.getDoctorName());

        String response = jwtTokenProvider.generateToken(claims.getDoctorEmail(),claims);
        assertThat(response).isNotNull();

        String name = jwtTokenProvider.getUsernameFromJWT(response);
        System.out.println(name);
        assertEquals(claims.getDoctorEmail(),name);
    }


    @Test
    void getIdFromToken_SUCCESS() {
        Login login = new Login();
        login.setEmail("sagar.singh@nineleaps.com");
        login.setId(2L);
        login.setUsername("sagar24");
        login.setRole("DOCTOR");
        login.setProfilePic("profilePic");

        Claims claims = new Claims();
        claims.setDoctorId(login.getId());
        claims.setDoctorEmail(login.getEmail());
        claims.setDoctorName(login.getUsername());
        claims.setRole(login.getRole());
        claims.setProfilePic(login.getProfilePic());

        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "encryption");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", 86400000);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(claims.getDoctorName());
        String token = jwtTokenProvider.generateToken(claims.getDoctorEmail(),claims);
        System.out.println(token);
        String newToken = "Bearer "+token;
        HttpServletRequest request = mock(HttpServletRequest.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn(newToken);

        Long id = jwtTokenProvider.getIdFromToken(request);
        assertAll(
                ()->assertThat(id).isNotNull(),
                ()->assertEquals(claims.getDoctorId(),id)
        );
    }




    @Test
    void getIdFromToken_tokenDoesNotStartsWithBearer() {
        Login login = new Login();
        login.setEmail("sagar.singh@nineleaps.com");
        login.setId(2L);
        login.setUsername("sagar24");
        login.setRole("DOCTOR");
        login.setProfilePic("profilePic");

        Claims claims = new Claims();
        claims.setDoctorId(login.getId());
        claims.setDoctorEmail(login.getEmail());
        claims.setDoctorName(login.getUsername());
        claims.setRole(login.getRole());
        claims.setProfilePic(login.getProfilePic());

        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(claims.getDoctorName());
        String token = jwtTokenProvider.generateToken(claims.getDoctorEmail(),claims);
        System.out.println(token);
        HttpServletRequest request = mock(HttpServletRequest.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn(token);

        APIException apiException =assertThrows(APIException.class,()->{
            jwtTokenProvider.getIdFromToken(request);
        });

        assertAll(
                ()->assertThat(apiException).isNotNull(),
                ()->assertEquals("JWT claims string is empty.",apiException.getMessage())
        );
    }

    @Test
    void getIdFromToken_tokenIsNull() {
        Login login = new Login();
        login.setEmail("xyz@nineleaps.com");
        login.setId(2L);
        login.setUsername("xyz");
        login.setRole("DOCTOR");
        login.setProfilePic("profilePic");

        Claims claims = new Claims();
        claims.setDoctorId(login.getId());
        claims.setDoctorEmail(login.getEmail());
        claims.setDoctorName(login.getUsername());
        claims.setRole(login.getRole());
        claims.setProfilePic(login.getProfilePic());

        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(claims.getDoctorName());
        String token = jwtTokenProvider.generateToken(claims.getDoctorEmail(),claims);
        System.out.println(token);
        HttpServletRequest request = mock(HttpServletRequest.class);

        Mockito.when(request.getHeader("Authorize")).thenReturn(token);

        APIException apiException =assertThrows(APIException.class,()->{
            jwtTokenProvider.getIdFromToken(request);
        });

        assertAll(
                ()->assertThat(apiException).isNotNull(),
                ()->assertEquals("JWT claims string is empty.",apiException.getMessage())
        );
    }


    @Test
    void validateToken_ThrowErrorForSignatureException(){

        HttpServletRequest request=mock(HttpServletRequest.class);
        String token1 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiRG9jdG9yRGV0YWlscyI6eyJkb2N0b3JJZCI6MywiZG9jdG9yTmFtZSI6InNhZ2FyIiwiZG9jdG9yRW1haWwiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwicm9sZSI6IkRPQ1RPUiIsInByb2ZpbGVQaWMiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BSXRidm1tcVhTdGZ4RVhObHJfT3U1ZDliU21Jd0ZfNVFzX3g5Q0ZOTFVvPXM5Ni1jIn0sInJvbGUiOiJET0NUT1IiLCJleHAiOjE2NTcxOTgwOTksImlhdCI6MTY1NzExMTY5OX0.D5_wpdm7P_Vk9gnDTlJ6D-TY8MI12A-ZFGzZje2D0H4";

        APIException apiException = assertThrows(APIException.class,()->{
            jwtTokenProvider.validateToken(token1,request);
        });

        assertAll(
                ()->assertThat(apiException).isNotNull(),
                ()->assertEquals("Invalid JWT token",apiException.getMessage())
        );
    }

    @Test
    void validateToken_ThrowErrorForMalformedJwtException(){

        String token2 = "eeyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiRG9jdG9yRGV0YWlscyI6eyJkb2N0b3JJZCI6MywiZG9jdG9yTmFtZSI6InNhZ2FyIiwiZG9jdG9yRW1haWwiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwicm9sZSI6IkRPQ1RPUiIsInByb2ZpbGVQaWMiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BSXRidm1tcVhTdGZ4RVhObHJfT3U1ZDliU21Jd0ZfNVFzX3g5Q0ZOTFVvPXM5Ni1jIn0sInJvbGUiOiJET0NUT1IiLCJleHAiOjE2NTcxOTgwOTksImlhdCI6MTY1NzExMTY5OX0.KSfiVzeFowbIBKDnAmlOEtvyXofymHbgT_bh_dghpVw-m0U6BRJnViDQT-1kWUwcM6AM5oqSDlRwsbiQ5RUIig";
        HttpServletRequest request=mock(HttpServletRequest.class);

        APIException apiException = assertThrows(APIException.class,()->{
            jwtTokenProvider.validateToken(token2,request);
        });

        assertAll(
                ()->assertThat(apiException).isNotNull(),
                ()->assertEquals("Invalid JWT token",apiException.getMessage())
        );

    }

    @Test
    void validateToken_ThrowErrorForExpiredJwtException(){
        String token3 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiRG9jdG9yRGV0YWlscyI6eyJkb2N0b3JJZCI6MywiZG9jdG9yTmFtZSI6InByYW5heSIsImRvY3RvckVtYWlsIjoicHJhbmF5Lm5hcmVkZHlAbmluZWxlYXBzLmNvbSIsInJvbGUiOiJET0NUT1IiLCJwcm9maWxlUGljIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUl0YnZtbXFYU3RmeEVYTmxyX091NWQ5YlNtSXdGXzVRc194OUNGTkxVbz1zOTYtYyJ9LCJyb2xlIjoiRE9DVE9SIiwiZXhwIjoxNjU3MTk4MDk5LCJpYXQiOjE2NTcxMTE2OTl9.LLZpLmbcoixOqbl52pgQjWNPIXOMT8SLjyfPMMs_9YzJeqzjK8dDvWviWZw6J3i7-V-EOCMiUCWdnQlw92BMmA";
        HttpServletRequest request=mock(HttpServletRequest.class);
        StringBuffer stringBuffer= new StringBuffer("/get-all");
        Mockito.when(request.getHeader("isRefreshToken")).thenReturn("");
        Mockito.when(request.getRequestURL()).thenReturn(stringBuffer);
        ExpiredJwtException apiException = assertThrows(ExpiredJwtException.class,()->{
            jwtTokenProvider.validateToken(token3,request);
        });

        assertAll(
                ()->assertThat(apiException).isNotNull(),
                ()->assertEquals("Expired JWT token",apiException.getMessage())
        );

    }

    @Test
    void validateToken_ThrowErrorForUnsupportedException(){
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
        HttpServletRequest request=mock(HttpServletRequest.class);

        Authentication authentication = mock(Authentication.class);

        Mockito.when(authentication.getName()).thenReturn(claims.getDoctorName());

        String token4 = jwtTokenProvider.generateToken(claims.getDoctorEmail(),claims);
        String finalToken=token4.substring(0,token4.lastIndexOf(".")+1);

        APIException apiException = assertThrows(APIException.class,()->{
            jwtTokenProvider.validateToken(finalToken,request);
        });

        assertAll(
                ()->assertThat(apiException).isNotNull(),
                ()->assertEquals("Invalid JWT token",apiException.getMessage())
        );

    }

    @Test
    void validateToken_ThrowErrorForIllegalArgumentException(){
        HttpServletRequest request=mock(HttpServletRequest.class);

        APIException apiException = assertThrows(APIException.class,()->{
            jwtTokenProvider.validateToken(null,request);
        });

        assertAll(
                ()->assertThat(apiException).isNotNull(),
                ()->assertEquals("Invalid JWT token",apiException.getMessage())
        );

    }
}