package com.dashboard.doctor_dashboard.services.login_service;

import com.dashboard.doctor_dashboard.entities.login_entity.LoginDetails;
import com.dashboard.doctor_dashboard.exceptions.GoogleLoginException;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
import com.dashboard.doctor_dashboard.jwt.service.JwtService;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map;

/**
 * LoginServiceImpl
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    private LoginRepo loginRepo;

    private JwtService jwtService;

    @Autowired
    public LoginServiceImpl(LoginRepo loginRepo, JwtService jwtService) {
        this.loginRepo = loginRepo;
        this.jwtService = jwtService;
    }

    private final String[] fields = {"given_name","hd", "email","picture"};

    /**
     * This function is for adding user into the database
     * @param loginDetails this variable contains Login details.
     * @return It returns a Map<String, Object>.
     */
    public boolean addUser(Map<String, Object> loginDetails) {
        log.info("inside: LoginServiceImpl::addUser");
        var doctorLoginDetails = loginRepo.findByEmailId(loginDetails.get(fields[2]).toString());
        boolean flag;
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
            log.debug(Constants.LOGIN +": User added");
            flag = true;
        } else {
            log.debug(Constants.LOGIN+": Existing user");
            flag = false;
        }
        log.info("exit: LoginServiceImpl::updateDoctor");

        return flag;
    }

    /**
     * This function of service is for verification of google token and then returns jwt token
     * @param idTokenString this variable contain Id token String.
     * @return It returns a ResponseEntity<GenericMessage> with status code 201 and a Jwt Token.
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws JSONException
     */
    //Token verification
    public ResponseEntity<GenericMessage> tokenVerification(String idTokenString) throws GeneralSecurityException, IOException, JSONException {
        log.info("inside: LoginServiceImpl::addUser");
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList("66297814659-gkj68lfu116ai19tb6e2rfacqt9bja0s.apps.googleusercontent.com", "866430808019-d5872q91thgcf3k52afir72g1autpjpn.apps.googleusercontent.com"))
                .build();
        GoogleIdToken idToken = verifier.verify(idTokenString);
        String jwtToken=takingInfoFromToken(idToken);
        log.info("exit: LoginServiceImpl::tokenVerification");
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,new JSONObject().put("jwt_token",jwtToken).toMap()), HttpStatus.CREATED);
    }

    /**
     * @param idToken this variable contain Id token.
     * @return It returns a Jwt token.
     */
    public String takingInfoFromToken(GoogleIdToken idToken) {
        log.info("inside: LoginServiceImpl::takingInfoFromToken");
        if (idToken != null) {
            log.debug(Constants.LOGIN+": token verified");
            var payload = idToken.getPayload();
            String email = payload.getEmail();
            var name = payload.get("given_name").toString();

            addUser(payload);
            long id = loginRepo.getId(email);
            log.info("exit: LoginServiceImpl::takingInfoFromToken");

            return loginCreator(id, email, name,loginRepo.getRoleById(id),loginRepo.getProfilePic(id));

        }
        log.debug(Constants.LOGIN+"::takingInfoFromToken"+": login failed due to Invalid ID token.");
        throw new GoogleLoginException("Invalid ID token.");
    }


    /**
     * This function of service is for creating jwt token
     * @param id this variable contain Id .
     * @param email this variable contain email.
     * @param name this variable contain name.
     * @param role this variable contain role.
     * @param profilePic this variable contain profile picture.
     * @return  It returns a Jwt token.
     */
    @Override
    public String loginCreator(long id, String email, String name,String role,String profilePic) {
        log.info("inside: LoginServiceImpl::loginCreator");

        var login = new Login();
        login.setId(id);
        login.setEmail(email);
        login.setUsername(name);
        login.setRole(role);
        login.setProfilePic(profilePic);
        log.info("exit: LoginServiceImpl::loginCreator");

        return jwtService.authenticateUser(login);

    }

    /**
     * This function of service is for deleting user.
     * @param id this variable contain Id .
     * @return  It returns a Jwt token.
     */
    @Override
    public String deleteDoctorById(long id) {
        log.info("inside: LoginServiceImpl::deleteDoctorById");
        loginRepo.deleteById(id);
        log.info("exit: LoginServiceImpl::deleteDoctorById");
        return "Successfully deleted";
    }

}
