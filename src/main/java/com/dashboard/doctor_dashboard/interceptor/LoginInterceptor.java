package com.dashboard.doctor_dashboard.interceptor;

import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


    private JwtTokenProvider tokenProvider;

    private LoginRepo loginRepo;

    @Autowired
    public LoginInterceptor(JwtTokenProvider tokenProvider, LoginRepo loginRepo) {
        this.tokenProvider = tokenProvider;
        this.loginRepo = loginRepo;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            var userId = Integer.parseInt(request.getHeader("userId"));
            String jwtToken;
            String bearerToken = request.getHeader("Authorization");
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                jwtToken = bearerToken.substring(7);
                tokenProvider.validateToken(jwtToken, request);
                String username = tokenProvider.getUsernameFromJWT(jwtToken);
                int id = loginRepo.getId(username);
                if (userId == id) {
                    log.debug("token successfully authenticated");
                    return true;
                }
                throw new APIException(Constants.UNAUTHORIZED);
            }else{
                throw new APIException("JWT claims string is empty.");
            }
        } catch (NumberFormatException e){

            throw new APIException("userId is missing in header!!!");
      }
   }


}
