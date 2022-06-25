package com.dashboard.doctor_dashboard.jwt.security;


import com.dashboard.doctor_dashboard.entities.login_entity.LoginDetails;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginRepo loginRepo;


    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        LoginDetails user = loginRepo.findByNameOrEmailId(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));

        return new MyUserDetails(user);

//        return new org.springframework.security.core.userdetails.User(
//                user.getUserName(),
//                user.getUserPassword(),
//                getAuthority(user)
//        );
//        return new org.springframework.security.core.userdetails.User(user.getEmailId(), user.getName(),
//                getAuthority(user));

    }
//    private Set getAuthority(LoginDetails user) {
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
////        user.getRole().forEach(role -> {
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
////        });
//        return authorities;
//    }

}

