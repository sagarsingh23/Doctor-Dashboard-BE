package com.dashboard.doctor_dashboard.Service;


import com.dashboard.doctor_dashboard.Entity.DoctorDetails;
import com.dashboard.doctor_dashboard.Entity.login_entity.DoctorLoginDetails;
import com.dashboard.doctor_dashboard.Repository.LoginRepo;
import com.dashboard.doctor_dashboard.Service.doctor_service.DoctorService;
import com.dashboard.doctor_dashboard.Service.login_service.LoginServiceImpl;
import com.dashboard.doctor_dashboard.jwt.Entity.Login;
import com.dashboard.doctor_dashboard.jwt.Service.JwtServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.json.webtoken.JsonWebSignature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginserviceTest {

    @Mock
    LoginRepo loginRepo;

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
    public void addingNewUser(){

        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("email","pranay@gmail.com");
        docDetails.put("given_name","pranay");
        docDetails.put("family_name","Reddy");
        docDetails.put("hd","nineleaps");
        DoctorLoginDetails doctorLoginDetails=new DoctorLoginDetails(1L,"Pranay","Reddy","pranay@gmail.com","nineleaps");

        Mockito.when(loginRepo.findByEmailId(doctorLoginDetails.getEmailId())).thenReturn(null);
        Mockito.when(loginRepo.save(doctorLoginDetails)).thenReturn(doctorLoginDetails);

        Boolean f=loginService.addUser(docDetails);
        assertEquals(true,f);

    }

    @Test
    public void CheckingExistingUser(){

        Map<String,Object> docDetails= new HashMap<>();
        docDetails.put("email","pranay@gmail.com");
        docDetails.put("given_name","pranay");
        docDetails.put("family_name","Reddy");
        docDetails.put("hd","nineleaps");
        DoctorLoginDetails doctorLoginDetails=new DoctorLoginDetails(1L,"Pranay","Reddy","pranay@gmail.com","nineleaps");

        Mockito.when(loginRepo.findByEmailId(doctorLoginDetails.getEmailId())).thenReturn(doctorLoginDetails);
//        Mockito.doNothing().when()
        Mockito.when(loginRepo.save(doctorLoginDetails)).thenReturn(doctorLoginDetails);

        Boolean f=loginService.addUser(docDetails);
        assertEquals(f,false);
    }

    @Mock
    DoctorService doctorService;
    @Test
    public void newUserInfoFromGoogleToken(){
        JsonWebSignature.Header header=new JsonWebSignature.Header();
        GoogleIdToken.Payload payload=new GoogleIdToken.Payload();
        payload.set("email","pranay@gmail.com");
        payload.set("given_name","pranay");
        payload.set("family_name","Reddy");
        byte[] b = new byte[5];
        DoctorDetails newDoctor = new DoctorDetails();
        newDoctor.setId(1L);
        newDoctor.setFirstName("pranay");
        newDoctor.setLastName("Reddy");
        newDoctor.setEmail("pranay@gmail.com");
        Mockito.doReturn(true).when(loginService).addUser(Mockito.any());
        Mockito.when(loginRepo.getId("pranay@gmail.com")).thenReturn(1);
        Mockito.when(doctorService.addDoctor(Mockito.any())).thenReturn(newDoctor);
        Mockito.doReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag").when(loginService).loginCreator(newDoctor.getId() , newDoctor.getEmail(),newDoctor.getFirstName());
        GoogleIdToken idToken=new GoogleIdToken(header,payload,b,b);
        String expected=loginService.takingInfoFromToken(idToken);
        assertEquals(expected,"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag");
    }
    @Test
    public void existingUserInfoFromGoogleToken(){
        JsonWebSignature.Header header=new JsonWebSignature.Header();
        GoogleIdToken.Payload payload=new GoogleIdToken.Payload();
        payload.set("email","pranay@gmail.com");
        payload.set("given_name","pranay");
        payload.set("family_name","Reddy");
        byte[] b = new byte[5];
        DoctorDetails newDoctor = new DoctorDetails();
        newDoctor.setId(1L);
        newDoctor.setFirstName("pranay");
        newDoctor.setLastName("Reddy");
        newDoctor.setEmail("pranay@gmail.com");
        Mockito.doReturn(false).when(loginService).addUser(Mockito.any());
        Mockito.when(loginRepo.getId("pranay@gmail.com")).thenReturn(1);
        Mockito.doReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag").when(loginService).loginCreator(newDoctor.getId() , newDoctor.getEmail(),newDoctor.getFirstName());

        GoogleIdToken idToken=new GoogleIdToken(header,payload,b,b);
        String expected=loginService.takingInfoFromToken(idToken);
        assertEquals(expected,"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag");
    }
    @Test
    public void InvalidGoogleToken(){
        String expected=loginService.takingInfoFromToken(null);
        assertEquals(expected,"ID token expired.");
    }


    @Mock
    JwtServiceImpl jwtService;
    @Test
    public void loginCreator(){
//        Map<String,Object> docDetails= new HashMap<>();
//        docDetails.put("email","pranay@gmail.com");
//        docDetails.put("given_name","pranay");
//        docDetails.put("family_name","Reddy");
//        docDetails.put("hd","nineleaps");
        Login login =new Login(1L,"pranay","pranay@gmail.com");
        Mockito.doReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag").when(jwtService).authenticateUser(Mockito.any());

        String actual=loginService.loginCreator(1L,"pranay@gmail.com","pranay");
        assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmFuYXkubmFyZWRkeUBuaW5lbGVhcHMuY29tIiwiaWF0IjoxNjQ5NTczMDIzLCJleHAiOjE2NDk2NTk0MjMsIkRvY3RvckRldGFpbHMiOnsiZG9jdG9ySWQiOjEsImRvY3Rvck5hbWUiOiJwcmFuYXkiLCJkb2N0b3JFbWFpbCI6InByYW5heS5uYXJlZGR5QG5pbmVsZWFwcy5jb20ifX0.udyr6ov047PEjYaGWR691WZWGqfuwrm9pN-NWtMFjAv-rJLHuDEd49ia4ibvSM3OhgW8C7VmC3CnI5Zy4QwNag",actual);
    }
}
