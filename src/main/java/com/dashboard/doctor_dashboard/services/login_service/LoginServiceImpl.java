package com.dashboard.doctor_dashboard.services.login_service;

import com.dashboard.doctor_dashboard.entities.login_entity.LoginDetails;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
import com.dashboard.doctor_dashboard.jwt.service.JwtService;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.services.doctor_service.DoctorService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private DoctorService doctorService;

    Logger logger= Logger.getLogger(LoginServiceImpl.class.getName());

    private final String[] fields = {"given_name","hd", "email","picture"};

    public boolean addUser(Map<String, Object> loginDetails) {
        boolean flag;
        logger.log(Level.INFO,"email={0}",loginDetails.get("email"));

        var doctorLoginDetails = loginRepo.findByEmailId(loginDetails.get(fields[2]).toString());
        if (doctorLoginDetails == null) {
            var newDoctor = new LoginDetails();
            newDoctor.setName(loginDetails.get(fields[0]).toString());
            if(loginDetails.get(fields[1])!=null && loginDetails.get(fields[1]).equals("nineleaps.com")) {
                newDoctor.setDomain(loginDetails.get(fields[1]).toString());
                newDoctor.setRole("DOCTOR");
            }
            else{
                newDoctor.setDomain("google");
                newDoctor.setRole("PATIENT");
            }
            newDoctor.setEmailId(loginDetails.get(fields[2]).toString());
            newDoctor.setProfilePic(loginDetails.get(fields[3]).toString());
            loginRepo.save(newDoctor);
            log.debug("Login:: User Added");
            flag = true;
        } else {
            log.debug("Login:: Existing User");
            flag = false;
        }
        return flag;
    }

    //Token verification
    public String tokenVerification(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList("66297814659-gkj68lfu116ai19tb6e2rfacqt9bja0s.apps.googleusercontent.com", "866430808019-d5872q91thgcf3k52afir72g1autpjpn.apps.googleusercontent.com"))
                .build();
        GoogleIdToken idToken = verifier.verify(idTokenString);
        return takingInfoFromToken(idToken);
    }

    public String takingInfoFromToken(GoogleIdToken idToken) {

        if (idToken != null) {
            log.debug("Login:: token verified!!");
            var payload = idToken.getPayload();
            String email = payload.getEmail();
            logger.log(Level.INFO,"email={0}" ,email);
            var name = payload.get("given_name").toString();
            addUser(payload);
            long id = loginRepo.getId(email);
            return loginCreator(id, email, name,loginRepo.getRoleById(id),loginRepo.getProfilePic(id));

        } else {
            log.debug("Login:: Login Failed..Invalid ID token");
        }
        return "ID token expired.";
    }

    @Autowired
    JwtService jwtService;

    @Override
    public String loginCreator(long id, String email, String name,String role,String profilePic) {
        var login = new Login();
        login.setId(id);
        login.setEmail(email);
        login.setUsername(name);
        login.setRole(role);
        login.setProfilePic(profilePic);
        return jwtService.authenticateUser(login);
    }

    @Override
    public String deleteDoctorById(long id) {
        loginRepo.deleteById(id);
        return "Successfully deleted";
    }


}
