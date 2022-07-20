package com.dashboard.doctor_dashboard.entities.login_entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class JwtToken {//Auth

    @NotNull
    @NotEmpty
    private String token;

    public String getIdtoken() {
        return token;
    }

    public void setIdtoken(String token) {
        this.token = token;
    }
}
