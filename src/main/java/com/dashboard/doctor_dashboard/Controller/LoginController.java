package com.dashboard.doctor_dashboard.Controller;


import com.dashboard.doctor_dashboard.Entity.login_entity.Id_Token;
import com.dashboard.doctor_dashboard.Service.login_service.LoginService;
import com.dashboard.doctor_dashboard.exception.GoogleLoginException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "api/doctor/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loginIdToken(@RequestBody Id_Token idToken) throws GeneralSecurityException, IOException, JSONException {
        Id_Token jwt = new Id_Token();
        jwt.setIdtoken(loginService.tokenVerification(idToken.getIdtoken()));
        JSONObject jsonObject = new JSONObject();
        if (!jwt.getIdtoken().equals("ID token expired.")) {
            jsonObject.put("jwt_token", jwt.getIdtoken());
            return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
        }
        throw new GoogleLoginException(jwt.getIdtoken());
    }
}
