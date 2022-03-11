package com.dashboard.doctor_dashboard.jwt.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AuthenticationResponse {
    private String accessToken;
}