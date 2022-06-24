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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private DoctorService doctorService;
    private boolean flag;

    Logger logger= Logger.getLogger(LoginServiceImpl.class.getName());

    private final String[] fields = {"given_name","hd", "email","picture"};

    public boolean addUser(Map<String, Object> loginDetails) {
        logger.log(Level.INFO,"email={0}",loginDetails.get("email"));

        var doctorLoginDetails = loginRepo.findByEmailId(loginDetails.get(fields[2]).toString());
        if (doctorLoginDetails == null) {
            var newDoctor = new LoginDetails();
//            newDoctor.setFirstName(loginDetails.get(fields[0]).toString());
//            newDoctor.setLastName(loginDetails.get(fields[1]).toString());
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
//            if(newDoctor.getDomain().equals("nineleaps.com")){
//                newDoctor.setRole("DOCTOR");
//            }else if(newDoctor.getDomain().equals("google")){
//                newDoctor.setRole("PATIENT");
//            }
            loginRepo.save(newDoctor);
            logger.log(Level.INFO,"User added");
            flag = true;
        } else {
            logger.log(Level.INFO,"Existing user");
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
            var payload = idToken.getPayload();
            System.out.println("payload="+payload);
            String email = payload.getEmail();
            logger.log(Level.INFO,"email={0}" ,email);
            var name = payload.get("given_name").toString();
//                    payload.get("given_name").toString().concat(" "+payload.get("family_name").toString());
//            var lastName = payload.get("family_name").toString();

            addUser(payload);
            long id = loginRepo.getId(email);
//            if (flag) {
//                var newDoctor = new DoctorDetails();
//                newDoctor.setId(id);
//                newDoctor.setFirstName(firstName);
//                newDoctor.setLastName(lastName);
//                newDoctor.setEmail(email);
//                doctorService.addDoctor(newDoctor);
//                logger.log(Level.INFO,"doctor details {0}",newDoctor);
//
//            }
            return loginCreator(id, email, name,loginRepo.getRoleById(id),loginRepo.getProfilePic(id));

        } else {
            logger.log(Level.WARNING,"Invalid ID token.");
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
        System.out.println("in");
        loginRepo.deleteById(id);
        return "Successfully deleted";
    }


}
