package com.dashboard.doctor_dashboard.jwt.Service;

import com.dashboard.doctor_dashboard.jwt.Entity.AuthenticationResponse;
import com.dashboard.doctor_dashboard.jwt.Entity.DoctorClaims;
import com.dashboard.doctor_dashboard.jwt.Entity.Login;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    public String authenticateUser(Login login) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.getEmail(), login.getUsername()));

        SecurityContextHolder.getContext().setAuthentication(authentication);


        // get token form tokenProvider

        DoctorClaims doctorClaims = new DoctorClaims();
        doctorClaims.setDoctorEmail(login.getEmail());
        doctorClaims.setDoctorName(login.getUsername());
        doctorClaims.setDoctorId(login.getId());


        String token = jwtTokenProvider.generateToken(authentication, doctorClaims);

        return new AuthenticationResponse(token).getAccessToken();
    }


}



