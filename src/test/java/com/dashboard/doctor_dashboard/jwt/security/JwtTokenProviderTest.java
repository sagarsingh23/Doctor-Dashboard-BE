package com.dashboard.doctor_dashboard.jwt.security;

import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.jwt.entities.Claims;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateTokenAndGetUsernameFromTokenTest_SUCCESS() {
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

        Authentication authentication = mock(Authentication.class);

        Mockito.when(authentication.getName()).thenReturn(claims.getDoctorName());

        String response = jwtTokenProvider.generateToken(authentication,claims);
        System.out.println(response);
        assertThat(response).isNotNull();

        String name = jwtTokenProvider.getUsernameFromJWT(response);
        System.out.println(name);
        assertEquals(claims.getDoctorName(),name);
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

        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(claims.getDoctorName());
        String token = jwtTokenProvider.generateToken(authentication,claims);
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
        String token = jwtTokenProvider.generateToken(authentication,claims);
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
        String token = jwtTokenProvider.generateToken(authentication,claims);
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

        String token1 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiRG9jdG9yRGV0YWlscyI6eyJkb2N0b3JJZCI6MywiZG9jdG9yTmFtZSI6InNhZ2FyIiwiZG9jdG9yRW1haWwiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwicm9sZSI6IkRPQ1RPUiIsInByb2ZpbGVQaWMiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BSXRidm1tcVhTdGZ4RVhObHJfT3U1ZDliU21Jd0ZfNVFzX3g5Q0ZOTFVvPXM5Ni1jIn0sInJvbGUiOiJET0NUT1IiLCJleHAiOjE2NTcxOTgwOTksImlhdCI6MTY1NzExMTY5OX0.D5_wpdm7P_Vk9gnDTlJ6D-TY8MI12A-ZFGzZje2D0H4";

        APIException apiException = assertThrows(APIException.class,()->{
            jwtTokenProvider.validateToken(token1);
        });

        assertAll(
                ()->assertThat(apiException).isNotNull(),
                ()->assertEquals("Invalid JWT signature",apiException.getMessage())
        );
    }

    @Test
    void validateToken_ThrowErrorForMalformedJwtException(){

        String token2 = "eeyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiRG9jdG9yRGV0YWlscyI6eyJkb2N0b3JJZCI6MywiZG9jdG9yTmFtZSI6InNhZ2FyIiwiZG9jdG9yRW1haWwiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwicm9sZSI6IkRPQ1RPUiIsInByb2ZpbGVQaWMiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BSXRidm1tcVhTdGZ4RVhObHJfT3U1ZDliU21Jd0ZfNVFzX3g5Q0ZOTFVvPXM5Ni1jIn0sInJvbGUiOiJET0NUT1IiLCJleHAiOjE2NTcxOTgwOTksImlhdCI6MTY1NzExMTY5OX0.KSfiVzeFowbIBKDnAmlOEtvyXofymHbgT_bh_dghpVw-m0U6BRJnViDQT-1kWUwcM6AM5oqSDlRwsbiQ5RUIig";

        APIException apiException = assertThrows(APIException.class,()->{
            jwtTokenProvider.validateToken(token2);
        });

        assertAll(
                ()->assertThat(apiException).isNotNull(),
                ()->assertEquals("Invalid JWT token",apiException.getMessage())
        );

    }

    @Test
    void validateToken_ThrowErrorForExpiredJwtException(){
        String token3 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiRG9jdG9yRGV0YWlscyI6eyJkb2N0b3JJZCI6MywiZG9jdG9yTmFtZSI6InByYW5heSIsImRvY3RvckVtYWlsIjoicHJhbmF5Lm5hcmVkZHlAbmluZWxlYXBzLmNvbSIsInJvbGUiOiJET0NUT1IiLCJwcm9maWxlUGljIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUl0YnZtbXFYU3RmeEVYTmxyX091NWQ5YlNtSXdGXzVRc194OUNGTkxVbz1zOTYtYyJ9LCJyb2xlIjoiRE9DVE9SIiwiZXhwIjoxNjU3MTk4MDk5LCJpYXQiOjE2NTcxMTE2OTl9.LLZpLmbcoixOqbl52pgQjWNPIXOMT8SLjyfPMMs_9YzJeqzjK8dDvWviWZw6J3i7-V-EOCMiUCWdnQlw92BMmA";

        APIException apiException = assertThrows(APIException.class,()->{
            jwtTokenProvider.validateToken(token3);
        });

        assertAll(
                ()->assertThat(apiException).isNotNull(),
                ()->assertEquals("Expired JWT token",apiException.getMessage())
        );

    }

    @Test
    void validateToken_ThrowErrorForUnsupportedException(){
        String token4 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWdhcnNzbjIzQGdtYWlsLmNvbSIsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjUsImRvY3Rvck5hbWUiOiJTYWdhciIsImRvY3RvckVtYWlsIjoic2FnYXJzc24yM0BnbWFpbC5jb20iLCJyb2xlIjoiUEFUSUVOVCIsInByb2ZpbGVQaWMiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BSXRidm1tWWhFR1pnM3BkbW91MzFQajN3cEljWG5XMDZpbXRKMjR0TFRaTj1zOTYtYyJ9LCJyb2xlIjoiUEFUSUVOVCIsImV4cCI6MTY1NzYyNTcyNywiaWF0IjoxNjU3NTM5MzI3fQ.";
        APIException apiException = assertThrows(APIException.class,()->{
            jwtTokenProvider.validateToken(token4);
        });

        assertAll(
                ()->assertThat(apiException).isNotNull(),
                ()->assertEquals("Unsupported JWT token",apiException.getMessage())
        );

    }

    @Test
    void validateToken_ThrowErrorForIllegalArgumentException(){
        APIException apiException = assertThrows(APIException.class,()->{
            jwtTokenProvider.validateToken(null);
        });

        assertAll(
                ()->assertThat(apiException).isNotNull(),
                ()->assertEquals("JWT claims string is empty.",apiException.getMessage())
        );

    }
}