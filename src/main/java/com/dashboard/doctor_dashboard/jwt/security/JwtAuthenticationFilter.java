package com.dashboard.doctor_dashboard.jwt.security;


import com.dashboard.doctor_dashboard.utils.Constants;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@NoArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private  JwtTokenProvider tokenProvider;
    @Autowired
    private  CustomUserDetailsService customUserDetailsService;

    String status = Constants.FAIL;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

//        System.out.println("request "+request.getInputStream());
        // get JWT (token) from http request
        String token = getJWTFromRequest(request);
        // validate token
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token,request)) {
            // get username from token
            String username = tokenProvider.getUsernameFromJWT(token);
            // load user associated with token
            var userDetails = customUserDetailsService.loadUserByUsername(username);
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // set spring security
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        status = Constants.SUCCESS;
        }
        filterChain.doFilter(request, response);
    }

    // Bearer <accessToken>
    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;

    }


}
