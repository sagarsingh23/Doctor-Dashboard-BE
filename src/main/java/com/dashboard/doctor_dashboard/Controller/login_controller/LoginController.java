package com.dashboard.doctor_dashboard.Controller.login_controller;


import com.dashboard.doctor_dashboard.Entity.login_entity.Id_Token;
import com.dashboard.doctor_dashboard.Service.login_service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import java.security.GeneralSecurityException;

@CrossOrigin
@RestController
public class LoginController {

    @Autowired
    private LoginService login;

    @PostMapping(value = "api/doctor/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loginIdToken(@RequestBody Id_Token idToken, HttpServletResponse response) throws GeneralSecurityException, IOException, JSONException {
//        System.out.println(idToken.getIdtoken());
//        HttpHeaders responseHeaders = new HttpHeaders();
            Id_Token jwt =new Id_Token();
            jwt.setIdtoken(login.tokenVerification(idToken.getIdtoken()));
//            responseHeaders.set("jwt_token","jwt");
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Jwt_token", login.tokenVerification(idToken.getIdtoken()));
//        response.setHeader("jwt_token",jwt.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jwt_token",jwt.getIdtoken());
        return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);

    }
}
