package com.dashboard.doctor_dashboard.jwt.config;

import com.dashboard.doctor_dashboard.jwt.security.CustomAuthenticationEntryPoint;
import com.dashboard.doctor_dashboard.jwt.security.CustomUserDetailsService;
import com.dashboard.doctor_dashboard.jwt.security.JwtAuthenticationEntryPoint;
import com.dashboard.doctor_dashboard.jwt.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] ALLOWED_URL = {
            // Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/", "/csrf",
            "/api/user/login",
            "/api/patient/changeMessage/**",
            "/files/**",
            "/api/receptionist/**"


    };

    private static final String[] DOCTOR_URL={

            "/api/todolist/**",
            "api/appointment/getAllAppointments/doctor/*",
            "api/attribute/changeNotes/*",
            "api/appointment/*/activePatient",
            "/files/{id}",
    };
    private static final String[] PATIENT_URL={

            "api/appointment/getAllAppointments/patient/*",
            "api/appointment/patient",
            "api/appointment/*/patient",
            "/api/patient/upload/*",
            "/api/patient/*",
            "/files/{id}"
    };
    private static final String[] GET_API_PATIENT_URL={

    };
    private static final String[] GET_API_DOCTOR_URL={

    };
    private static final String[] PUT_API_DOCTOR_URL={

    };


    @Bean
    public Filter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(ALLOWED_URL).permitAll()
                .antMatchers(DOCTOR_URL).hasAuthority("DOCTOR")
                .antMatchers(PATIENT_URL).hasAuthority("PATIENT")
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);   //NOSONAR
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

//    @Bean
//    public AuthenticationEntryPoint authenticationEntryPoint(){
//        return new CustomAuthenticationEntryPoint();
//    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAuthenticationEntryPoint();
    }

}
