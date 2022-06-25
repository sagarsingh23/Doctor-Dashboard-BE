package com.dashboard.doctor_dashboard.jwt.security;


import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
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

//    @Override
//    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
//        ObjectMapper mapper = new ObjectMapper();
//        var jsonObject = new JSONObject();
//        try {
//            jsonObject.put("timestamp", System.currentTimeMillis());
//            jsonObject.put("status", 403);
//            jsonObject.put("message", "Access denied");
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//
//        res.setContentType("application/json;charset=UTF-8");
//        res.setStatus(403);
//
//        res.getWriter().write(mapper.writeValueAsString(jsonObject));
//    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
//        var jsonObject = new JSONObject();
        GenericMessage genericMessage=new GenericMessage();

//        try {
//            jsonObject.put("timestamp", System.currentTimeMillis());
//            jsonObject.put("status", 403);
//            jsonObject.put("message", "Access denied");
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
        Map<String,Object> error =new HashMap<>();
        error.put("timestamp", new Date().toString());
        error.put("status",403);
        error.put("message","Access denied");
        genericMessage.setStatus("Forbidden");
        genericMessage.setData(error);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);

        response.getWriter().write(mapper.writeValueAsString(genericMessage));

    }
}