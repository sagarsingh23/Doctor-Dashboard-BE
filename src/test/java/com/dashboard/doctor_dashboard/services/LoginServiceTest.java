package com.dashboard.doctor_dashboard.services;


import com.dashboard.doctor_dashboard.entities.login_entity.LoginDetails;
import com.dashboard.doctor_dashboard.exceptions.GoogleLoginException;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
import com.dashboard.doctor_dashboard.jwt.service.JwtServiceImpl;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorService;
import com.dashboard.doctor_dashboard.services.login_service.LoginServiceImpl;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.webtoken.JsonWebSignature;
import org.codehaus.jettison.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    LoginRepo loginRepo;

    @Mock
    JwtServiceImpl jwtService;

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
        docDetails.put("given_name","sagar");
        docDetails.put("hd","gmail.com");
        docDetails.put("email","sagar@gmail.com");
        docDetails.put("picture","picture2");

        Mockito.when(loginRepo.findByEmailId(Mockito.any(String.class))).thenReturn(null);
        Boolean f= loginService.addUser(docDetails);
        assertEquals(true,f);

    }

    @Test
    void checkIfTheDomainIsNullForAddUser(){

        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("given_name","gokul");
        docDetails.put("hd",null);
        docDetails.put("email","gokul@gmail.com");
        docDetails.put("picture","picture2");

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
        LoginDetails loginDetails=new LoginDetails(1L,"Pranay","pranay@gmail.com","nineleaps","profilePic1",null,false,null,null,null);

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
        String message = "Invalid ID token.";

        GoogleLoginException googleLoginException=assertThrows(GoogleLoginException.class,()->loginService.takingInfoFromToken(null));
        assertThat(googleLoginException).isNotNull();
        assertEquals(message,googleLoginException.getMessage());
    }


    @Test
    void tokenVerification() throws GeneralSecurityException, IOException, JSONException {
//        long id = 1L;
        String message = "Invalid ID token.";
        JsonWebSignature.Header header=new JsonWebSignature.Header();
        GoogleIdToken.Payload payload=new GoogleIdToken.Payload();
        payload.set("email","pranay@gmail.com");
        payload.set("given_name","pranay");
        payload.set("family_name","Reddy");
        payload.set("picture","picture1");
        byte[] b = new byte[5];
        GoogleIdToken idToken=new GoogleIdToken(header,payload,b,b);

        Login login =new Login(1L,"pranay","pranay@gmail.com","PATIENT","profilePic1");


        String idTokenString = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjYzMWZhZTliNTk0MGEyZDFmYmZmYjAwNDAzZDRjZjgwYTIxYmUwNGUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXpwIjoiNjYyOTc4MTQ2NTktZ2tqNjhsZnUxMTZhaTE5dGI2ZTJyZmFjcXQ5YmphMHMuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2NjI5NzgxNDY1OS1na2o2OGxmdTExNmFpMTl0YjZlMnJmYWNxdDliamEwcy5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwMzc4MTg5NTcwNzYzOTYyMzc1NiIsImhkIjoibmluZWxlYXBzLmNvbSIsImVtYWlsIjoic2FnYXIuc2luZ2hAbmluZWxlYXBzLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiOVowQjg5Q1lWaWlLd1RPUllLTmpzdyIsIm5hbWUiOiJzYWdhciBzaW5naCIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BSXRidm1sX1VPN25aUENRVDg5dmhYNmZjWWNwZmRPelc5LVlJTk1IVC04Zz1zOTYtYyIsImdpdmVuX25hbWUiOiJzYWdhciIsImZhbWlseV9uYW1lIjoic2luZ2giLCJsb2NhbGUiOiJlbiIsImlhdCI6MTY1ODQwMzY2MiwiZXhwIjoxNjU4NDA3MjYyLCJqdGkiOiJmNTA4NTFkMGJjYzkwM2I3MjBmZTg3M2E3MmMzZjllOTA5YWFiOTk3In0.f5DctC2X4hHokEbc3dnaA3qHXJl103ct05GIsJHdz7CSA3AQj4-mw8ZUzwtWwxcwNFLjOTzPrynRGWWo9u8g_xaQ8FXU6MD5M1MCJHriPoFEziOf0HYg1qwZNTxJRwQFZn9uT1jNA4qHMrOypBJK-D59NkDZ8gkSjKovgirbzlAAjPmXNgFIvy7tL5ZBzPWYTBNqPnPR6ClyVuhzvKFgprICLwBQKdw-wylmV0162pciG-qVcV9TgUunAjISW3gMyDri1WkoA5FXtZA2gi7INQHCxsWvPEiu-o_p2b4saVD91qJcwN3-8wg9BbSAUFquDF13ZL3XrUFzwcnEWardKg";
        String jwtToken="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiRG9jdG9yRGV0YWlscyI6eyJkb2N0b3JJZCI6MiwiZG9jdG9yTmFtZSI6InByYW5heSIsImRvY3RvckVtYWlsIjoicHJhbmF5Lm5hcmVkZHlAbmluZWxlYXBzLmNvbSIsInJvbGUiOiJET0NUT1IiLCJwcm9maWxlUGljIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUl0YnZtbXFYU3RmeEVYTmxyX091NWQ5YlNtSXdGXzVRc194OUNGTkxVbz1zOTYtYyJ9LCJyb2xlIjoiRE9DVE9SIiwiZXhwIjoxNjU4MzA4NDMwLCJpYXQiOjE2NTgyMjIwMzB9.XfCkZCob1Yfe1yVl1v5Xu7aHN0UdavHs-vZkdLPt9CO6TI1BYX13anXNVsn1Gnv8qLD2yHFmX4Cu-tJrRU3tyA";
        GoogleIdTokenVerifier verifier = mock(GoogleIdTokenVerifier.class);
        Mockito.when(verifier.verify(Mockito.any(String.class))).thenReturn(idToken);
        Mockito.when(loginRepo.getId(Mockito.any(String.class))).thenReturn(1);
        Mockito.when(loginRepo.getRoleById(Mockito.any(Long.class))).thenReturn("DOCTOR");
       Mockito.when(loginRepo.getProfilePic(Mockito.any(Long.class))).thenReturn("profile pic1");
        Mockito.when(jwtService.authenticateUser(Mockito.any(Login.class))).thenReturn(jwtToken);

        assertThrows(GoogleLoginException.class,()->loginService.tokenVerification(idTokenString));

//        ResponseEntity<GenericMessage> response = loginService.tokenVerification(idTokenString);
    }


    @Test
    void InvalidGoogleToken(){
        String message = "Invalid ID token.";
        GoogleLoginException googleLoginException=assertThrows(GoogleLoginException.class,()->loginService.takingInfoFromToken(null));
        assertThat(googleLoginException).isNotNull();
        assertEquals(message,googleLoginException.getMessage());
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
