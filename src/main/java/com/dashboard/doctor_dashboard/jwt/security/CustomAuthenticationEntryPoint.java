package com.dashboard.doctor_dashboard.jwt.security;


import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationEntryPoint implements AccessDeniedHandler {

    String status = Constants.FAIL;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        var mapper = new ObjectMapper();
        var genericMessage=new GenericMessage();
        Map<String,Object> error =new HashMap<>();
        error.put("timestamp", new Date().toString());
        error.put("status",403);
        error.put("message","Access denied");
        genericMessage.setStatus("Forbidden");
        genericMessage.setData(error);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);

        response.getWriter().write(mapper.writeValueAsString(genericMessage));

        status = Constants.SUCCESS;
    }
}