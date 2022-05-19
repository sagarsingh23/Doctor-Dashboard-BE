package com.dashboard.doctor_dashboard.services.login_service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Service
public interface LoginService {

    boolean addUser(Map<String, Object> loginDetails);

    String tokenVerification(String idTokenString) throws GeneralSecurityException, IOException;

    String takingInfoFromToken(GoogleIdToken idToken);

    String loginCreator(long id, String email, String firstName);

    String deleteDoctorById(long id);
}
