package com.dashboard.doctor_dashboard.Controller.login_controller;

import com.dashboard.doctor_dashboard.Entity.login_entity.Id_Token;
import com.dashboard.doctor_dashboard.Service.login_service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
public class LoginController {

    @Autowired
    LoginService login;

    @PostMapping("api/doctor/login")
    public String loginIdToken(@RequestBody Id_Token idToken) throws GeneralSecurityException, IOException {

        login.tokenVerification(idToken.getIdtoken());
        return "done";
    }
}
