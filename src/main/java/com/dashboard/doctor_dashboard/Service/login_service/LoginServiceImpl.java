package com.dashboard.doctor_dashboard.Service.login_service;

import com.dashboard.doctor_dashboard.Entity.doctor_entity.DoctorDetails;
import com.dashboard.doctor_dashboard.Entity.login_entity.DoctorLoginDetails;
import com.dashboard.doctor_dashboard.Repository.login_repo.LoginRepo;
import com.dashboard.doctor_dashboard.Service.doctor_service.DoctorService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginRepo loginRepo ;

    @Autowired
    private DoctorService doctorService;
    private boolean flag;

    public boolean addUser( Map<String ,Object> loginDetails){
         System.out.println("email="+loginDetails.get("email"));

         DoctorLoginDetails doctorLoginDetails=loginRepo.findByEmailId(loginDetails.get("email").toString());
         if(doctorLoginDetails==null){
              DoctorLoginDetails newDoctor = new DoctorLoginDetails();
             newDoctor.setFirstName(loginDetails.get("given_name").toString());
             newDoctor.setLastName(loginDetails.get("family_name").toString());
             newDoctor.setDomain(loginDetails.get("hd").toString());
             newDoctor.setEmailId(loginDetails.get("email").toString());
             loginRepo.save(newDoctor);
             System.out.println("User added");
             flag=true;
         }
         else {
             System.out.println("Existing user");
             flag=false;
         }
         return flag;
     }

     //Token verification
    public void tokenVerification(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList("66297814659-gkj68lfu116ai19tb6e2rfacqt9bja0s.apps.googleusercontent.com","866430808019-d5872q91thgcf3k52afir72g1autpjpn.apps.googleusercontent.com"))
                .build();
        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            Payload payload = idToken.getPayload();
            boolean flag=addUser(payload);
            if(flag==true){
                int id=loginRepo.getId(payload.get("email").toString());
                DoctorDetails newDoctor = new DoctorDetails();
                newDoctor.setId(id);
                newDoctor.setFirstName(payload.get("given_name").toString());
                newDoctor.setLastName(payload.get("family_name").toString());
                newDoctor.setEmail(payload.get("email").toString());
                doctorService.addDoctor(newDoctor);
                System.out.println(newDoctor);
            }
        }
        else {
            System.out.println("Invalid ID token.");
        }
    }
}
