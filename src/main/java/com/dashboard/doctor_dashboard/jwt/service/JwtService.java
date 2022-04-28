package com.dashboard.doctor_dashboard.jwt.service;

import com.dashboard.doctor_dashboard.jwt.entity.Login;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    String authenticateUser(Login login);
}
