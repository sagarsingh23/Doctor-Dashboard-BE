package com.dashboard.doctor_dashboard.services.login_service;

import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Service
public interface LoginService {

    boolean addUser(Map<String, Object> loginDetails);

    ResponseEntity<GenericMessage> tokenVerification(String idTokenString) throws GeneralSecurityException, IOException, JSONException;

    String takingInfoFromToken(GoogleIdToken idToken);

    String loginCreator(long id, String email, String firstName,String role,String profilePic);

    String deleteDoctorById(long id);
}
