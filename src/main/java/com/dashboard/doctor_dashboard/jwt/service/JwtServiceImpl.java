package com.dashboard.doctor_dashboard.jwt.service;

import com.dashboard.doctor_dashboard.jwt.entities.AuthenticationResponse;
import com.dashboard.doctor_dashboard.jwt.entities.Claims;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    /**
     * This function of service is for authenticating user and generating token
     * @param login contains fields email,role,profilePic etc
     * @return  jwt token and status code 201
     */
    public String authenticateUser(Login login) {
        log.info("inside: JwtServiceImpl::authenticateUser");
        String roles = login.getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roles));
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.getEmail(), login.getUsername(),authorities));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("auth"+authentication);

        var claims = new Claims();
        claims.setDoctorEmail(login.getEmail());
        claims.setDoctorName(login.getUsername());
        claims.setDoctorId(login.getId());
        claims.setRole(login.getRole());
        claims.setProfilePic(login.getProfilePic());

        String token = jwtTokenProvider.generateToken(authentication.getName(), claims);
        log.debug("JWTService: JWT token created.");
        log.info("exit: JwtServiceImpl::authenticateUser");
        return new AuthenticationResponse(token).getAccessToken();
    }

    @Override
    public String createRefreshToken(DefaultClaims defaultClaims){

    Map<String,Object> claims= new HashMap<>();

        claims.put("DoctorDetails",defaultClaims.get("DoctorDetails"));
        return jwtTokenProvider.doGenerateToken(defaultClaims.get("sub").toString(), claims);
    }


}



