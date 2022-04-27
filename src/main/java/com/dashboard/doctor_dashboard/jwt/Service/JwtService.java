package com.dashboard.doctor_dashboard.jwt.Service;

import com.dashboard.doctor_dashboard.jwt.Entity.Login;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    String authenticateUser(Login login);
}
