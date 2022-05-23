package com.dashboard.doctor_dashboard.jwt.service;

import com.dashboard.doctor_dashboard.jwt.entities.AuthenticationResponse;
import com.dashboard.doctor_dashboard.jwt.entities.Claims;
import com.dashboard.doctor_dashboard.jwt.entities.Login;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtServiceImpl implements JwtService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    public String authenticateUser(Login login) {
        String roles = login.getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roles));
        System.out.println("authorities "+authorities);
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.getEmail(), login.getUsername(),authorities));

        SecurityContextHolder.getContext().setAuthentication(authentication);


        // get token form tokenProvider

        var claims = new Claims();
        claims.setEmail(login.getEmail());
        claims.setName(login.getUsername());
        claims.setId(login.getId());
        claims.setRole(login.getRole());

        String token = jwtTokenProvider.generateToken(authentication, claims);

        return new AuthenticationResponse(token).getAccessToken();
    }


}



