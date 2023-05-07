package com.dashboard.doctor_dashboard.jwt.entities;

import lombok.AllArgsConstructor;


@AllArgsConstructor

public class AuthenticationResponse {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

}