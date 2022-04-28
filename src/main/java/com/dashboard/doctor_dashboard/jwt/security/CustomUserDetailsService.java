package com.dashboard.doctor_dashboard.jwt.security;


import com.dashboard.doctor_dashboard.entity.login_entity.DoctorLoginDetails;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginRepo loginRepo;


    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        DoctorLoginDetails user = loginRepo.findByFirstNameOrEmailId(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));
        return new org.springframework.security.core.userdetails.User(user.getEmailId(), user.getFirstName(),
                new ArrayList<>());

    }
//        public Collection<? extends GrantedAuthority> getAuthorities () {
//            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRoles());
//            return List.of(simpleGrantedAuthority);
//        }
}

