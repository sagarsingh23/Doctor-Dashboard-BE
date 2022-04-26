package com.dashboard.doctor_dashboard.jwt.Entity;

import lombok.AllArgsConstructor;


@AllArgsConstructor

public class AuthenticationResponse {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

}