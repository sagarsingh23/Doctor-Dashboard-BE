package com.dashboard.doctor_dashboard.services;


import com.dashboard.doctor_dashboard.entities.login_entity.LoginDetails;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
import com.dashboard.doctor_dashboard.jwt.service.JwtServiceImpl;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorService;
import com.dashboard.doctor_dashboard.services.login_service.LoginServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.webtoken.JsonWebSignature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    LoginRepo loginRepo;

    @Mock
    JwtServiceImpl jwtService;

    @Mock
    DoctorService doctorService;

    @InjectMocks @Spy
    LoginServiceImpl loginService;

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
    void addingNewUser_SUCCESS(){

        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("given_name","pranay");
        docDetails.put("hd","nineleaps.com");
        docDetails.put("email","pranay@gmail.com");
        docDetails.put("picture","picture1");

        Mockito.when(loginRepo.findByEmailId(Mockito.any(String.class))).thenReturn(null);
        Boolean f= loginService.addUser(docDetails);
        assertEquals(true,f);

    }

    @Test
    void checkIfTheDomainIsNotNineleapsForAddUser(){
        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("given_name","pranay");
        docDetails.put("hd","gmail.com");
        docDetails.put("email","pranay@gmail.com");
        docDetails.put("picture","picture1");

        Mockito.when(loginRepo.findByEmailId(Mockito.any(String.class))).thenReturn(null);
        Boolean f= loginService.addUser(docDetails);
        assertEquals(true,f);

    }

    @Test
    void checkIfTheDomainIsNullForAddUser(){

        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("given_name","pranay");
        docDetails.put("hd",null);
        docDetails.put("email","pranay@gmail.com");
        docDetails.put("picture","picture1");

        Mockito.when(loginRepo.findByEmailId(Mockito.any(String.class))).thenReturn(null);
        Boolean f= loginService.addUser(docDetails);
        assertEquals(true,f);

    }

    @Test
    void CheckingExistingUser(){

        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("given_name","pranay");
        docDetails.put("hd","nineleaps.com");
        docDetails.put("email","pranay@gmail.com");
        docDetails.put("picture","picture1");
        LoginDetails loginDetails=new LoginDetails(1L,"Pranay","pranay@gmail.com","nineleaps","profilePic1",null,null,null,null);

        Mockito.when(loginRepo.findByEmailId(loginDetails.getEmailId())).thenReturn(loginDetails);

        Boolean f=loginService.addUser(docDetails);
        assertEquals(false,f);
    }


    @Test
    void newUserInfoFromGoogleToken(){
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUB";
        JsonWebSignature.Header header=new JsonWebSignature.Header();
        GoogleIdToken.Payload payload=new GoogleIdToken.Payload();
        payload.set("email","pranay@gmail.com");
        payload.set("given_name","pranay");
        payload.set("family_name","Reddy");
        payload.set("picture","picture1");
        byte[] b = new byte[5];
        GoogleIdToken idToken=new GoogleIdToken(header,payload,b,b);
        System.out.println(idToken);

        Mockito.when(jwtService.authenticateUser(Mockito.any(Login.class))).thenReturn(token);

        String expectedToken = loginService.takingInfoFromToken(idToken);
        assertEquals(token,expectedToken);
    }

    @Test
    void checkIfGoogleTokenIsNull(){
        String message = "ID token expired.";
        String expectedMessage = loginService.takingInfoFromToken(null);
        assertEquals(message,expectedMessage);
    }


    @Test
    void tokenVerification() throws GeneralSecurityException, IOException {
        String message = "ID token expired.";

        String idTokenString = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjI2NTBhMmNlNDdiMWFiM2JhNDA5OTc5N2Y4YzA2ZWJjM2RlOTI4YWMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXpwIjoiNjYyOTc4MTQ2NTktZ2tqNjhsZnUxMTZhaTE5dGI2ZTJyZmFjcXQ5YmphMHMuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2NjI5NzgxNDY1OS1na2o2OGxmdTExNmFpMTl0YjZlMnJmYWNxdDliamEwcy5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwMzc4MTg5NTcwNzYzOTYyMzc1NiIsImhkIjoibmluZWxlYXBzLmNvbSIsImVtYWlsIjoic2FnYXIuc2luZ2hAbmluZWxlYXBzLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiVFp1dkQ1UU1JRFVUemQtRkhOT3QxUSIsIm5hbWUiOiJzYWdhciBzaW5naCIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQVRYQUp4MkhDdEVrek9qQW5HUHh3QnlsOUxXNXRvYjdKZVhSYVc5amEyNT1zOTYtYyIsImdpdmVuX25hbWUiOiJzYWdhciIsImZhbWlseV9uYW1lIjoic2luZ2giLCJsb2NhbGUiOiJlbiIsImlhdCI6MTY1NjY1NjA4NiwiZXhwIjoxNjU2NjU5Njg2LCJqdGkiOiIxNDVmNGJlOTc3NDFiZjc2ZDc5YTg5MDhhZmVjMDg5Y2RmMzQ5NGE5In0.UfCnkqit2US5Cusp8S0UpsOVgn_8RPj88JubhEQ1wOv5m2Kgm_5dVXRQiVlNqmrR5QuUPfOmmkdwtIxX1jiOeVDfePMsgXe03rsLoLCukG12oR02b11yU4hbO4OpiM87H3VkyQ2wlfQEXESr18vYwmCXTf8RiI1crB36uXE_o4QMTk0R7aWT3ZFoKj7BXJwvtMQp2z-r-9jPf8jrBxdb19FZTd8sgmL_TQ1d2Eql4FeAxxAbJAvZBdac1x56BGJZzr-GJ2ApGCm77-0Lj7uaFa4AVkGITC_U9Zdnb4zTpXYifK1xcaVUvmMk5rdV0TBp2Zx2YM2Vm2qgDYcP5aWImw";
        GoogleIdTokenVerifier verifier = mock(GoogleIdTokenVerifier.class);

        Mockito.when(verifier.verify(Mockito.any(String.class))).thenReturn(null);
        String expectedMessage = loginService.tokenVerification(idTokenString);
        assertEquals(message,expectedMessage);

    }


//    @Test
//    void existingUserInfoFromGoogleToken(){
//        JsonWebSignature.Header header=new JsonWebSignature.Header();
//        GoogleIdToken.Payload payload=new GoogleIdToken.Payload();
//        payload.set("email","pranay@gmail.com");
//        payload.set("given_name","pranay");
//        payload.set("family_name","Reddy");
//        byte[] b = new byte[5];
//        DoctorDetails newDoctor = new DoctorDetails();
////        newDoctor.setId(1L);
////        newDoctor.setFirstName("pranay");
////        newDoctor.setLastName("Reddy");
////        newDoctor.setEmail("pranay@gmail.com");
//        Mockito.doReturn(false).when(loginService).addUser(Mockito.any());
//        Mockito.when(loginRepo.getId("pranay@gmail.com")).thenReturn(1);
//        Mockito.doReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag").when(loginService).
//                loginCreator(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class));
//
//        GoogleIdToken idToken=new GoogleIdToken(header,payload,b,b);
//        String value=loginService.takingInfoFromToken(idToken);
//        assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag",value);
//    }

    @Test
    void InvalidGoogleToken(){
        String value=loginService.takingInfoFromToken(null);
        assertEquals("ID token expired.",value);
    }


    @Test
    void loginCreator(){
        Login login =new Login(1L,"pranay","pranay@gmail.com","PATIENT","profilePic1");
        Mockito.doReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag").when(jwtService).authenticateUser(Mockito.any());

        String actual=loginService.loginCreator(1L,"pranay@gmail.com","pranay","PATIENT","profilePic1");
        assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag",actual);
    }

    @Test
    void deleteDoctorById(){
        final Long doctorId = 1L;
        loginService.deleteDoctorById(doctorId);
        loginService.deleteDoctorById(doctorId);

        verify(loginRepo,times(2)).deleteById(doctorId);
    }
}
