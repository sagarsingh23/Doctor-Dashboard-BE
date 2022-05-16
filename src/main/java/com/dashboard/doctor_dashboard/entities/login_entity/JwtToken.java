package com.dashboard.doctor_dashboard.entities.login_entity;

public class JwtToken {//Auth
    private String token;

    public String getIdtoken() {
        return token;
    }

    public void setIdtoken(String token) {
        this.token = token;
    }
}
