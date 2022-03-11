package com.dashboard.doctor_dashboard.Service.login_service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Service
public interface LoginService {

    public boolean addUser( Map<String ,Object> loginDetails);
    public String tokenVerification(String idTokenString) throws GeneralSecurityException, IOException;
    String loginCreator(long id,String email,String firstName);
}
