package com.dashboard.doctor_dashboard.controllers;


import com.dashboard.doctor_dashboard.entities.login_entity.JwtToken;
import com.dashboard.doctor_dashboard.services.login_service.LoginService;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
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
@RequestMapping("api/v1")
@Slf4j
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
    @PostMapping(value = "/user/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericMessage> tokenAuthentication(@Valid @RequestBody JwtToken idToken) throws GeneralSecurityException, IOException, JSONException {
        //authToken
        log.info("LoginController:: tokenAuthentication");
        return loginService.tokenVerification(idToken.getIdtoken());
    }

    /**
     * @return status 200 ok if the server is up and running.
     * It's just a health check API
     */
    @ApiOperation("This API will check if the server is up and running")
    @GetMapping(value = "/check")
    public ResponseEntity<String> checkServerStatus(){
        log.info("LoginController:: checkServerStatus");

        return new ResponseEntity<>(HttpStatus.OK);
    }
    /**
     * @param id is used as path variable
     * @return Successfully deleted message after deleting user details from database
     */
    @ApiOperation("This API is used for deleting user from login details")
    @DeleteMapping(value = "/user/login/delete/{id}")
    public String deleteUserById(@PathVariable("id") long id ){
        log.info("LoginController:: deleteDoctorById");
        return loginService.deleteDoctorById(id);
    }
}
