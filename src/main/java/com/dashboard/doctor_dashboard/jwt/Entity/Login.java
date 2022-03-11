package com.dashboard.doctor_dashboard.jwt.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {

    private Long id;
    private String username;
    private String email;
}
