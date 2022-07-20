package com.dashboard.doctor_dashboard.controllers;


import com.dashboard.doctor_dashboard.entities.login_entity.JwtToken;
import com.dashboard.doctor_dashboard.exceptions.GoogleLoginException;
import com.dashboard.doctor_dashboard.services.login_service.LoginService;
import io.swagger.annotations.ApiOperation;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class LoginController {


    private  LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * @param idToken contains google id token for Authenticating
     * @return Jwt token which will be used to access all the API
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws JSONException
     */
    @ApiOperation("This API will be used to login through Google SSO and returns jwt token")
    @PostMapping(value = "api/v1/user/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> tokenAuthentication(@Valid @RequestBody JwtToken idToken) throws GeneralSecurityException, IOException, JSONException {
        //authToken
        var jwt = new JwtToken();
        jwt.setIdtoken(loginService.tokenVerification(idToken.getIdtoken()));
        var jsonObject = new JSONObject();
        if (!jwt.getIdtoken().equals("ID token expired.")) {
            jsonObject.put("jwt_token", jwt.getIdtoken());
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.CREATED);
        }
        throw new GoogleLoginException(jwt.getIdtoken());
    }

    /**
     * @return status 200 ok if the server is up and running.
     * It's just a health check API
     */
    @ApiOperation("This API will check if the server is up and running")
    @GetMapping(value = "api/v1/check")
    public ResponseEntity<String> checkServerStatus(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param id is used as path variable
     * @return Successfully deleted message after deleting user details from database
     */
    @ApiOperation("This API is used for deleting user from login details")
    @DeleteMapping(value = "api/v1/user/login/delete/{id}")
    public String deleteUserById(@PathVariable("id") long id ){
        return loginService.deleteDoctorById(id);
    }
}
