package com.dashboard.doctor_dashboard.jwt.security;

import com.dashboard.doctor_dashboard.entities.LoginDetails;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class CustomUserDetailsServiceTest {

    @Mock
    private LoginRepo loginRepo;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }


    @Test
    void loadUserByUsernameTest() {

        String role = "DOCTOR";
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        LoginDetails loginDetails=new LoginDetails(1L,"Pranay","pranay@gmail.com","nineleaps","profilePic1",role,false,null,null,null);

        Mockito.when(loginRepo.findByNameOrEmailId(Mockito.any(String.class),Mockito.any(String.class))).thenReturn(Optional.of(loginDetails));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("sagarssn23@gmail.com");

        assertAll(
                ()->assertThat(userDetails).isNotNull(),
                ()->assertEquals(loginDetails.getEmailId(),userDetails.getUsername()),
                ()->assertEquals(loginDetails.getName(),userDetails.getPassword()),
                ()->assertEquals(authorities,userDetails.getAuthorities()),
                ()->assertEquals(true,userDetails.isEnabled()),
                ()->assertEquals(true,userDetails.isAccountNonExpired()),
                ()->assertEquals(true,userDetails.isCredentialsNonExpired()),
                ()->assertEquals(true,userDetails.isAccountNonLocked())

        );

    }

    @Test
    void throwErrorIfUserNotFound() {

        Mockito.when(loginRepo.findByNameOrEmailId(Mockito.any(String.class),Mockito.any(String.class))).thenReturn(Optional.empty());

        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class,()->{
            customUserDetailsService.loadUserByUsername("sagarssn23@gmail.com");
        });

        assertAll(
                ()->assertThat(usernameNotFoundException.getMessage()).isNotNull(),
                ()->assertEquals("User not found with username or email:sagarssn23@gmail.com",usernameNotFoundException.getMessage())
        );

    }
}