package com.sonic.sonic_backend.configuration.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final static String EXCEPTION = "exception";
    private final static String LOGOUT = "logout";
    private final static String MALFORMED = "malformed";
    private final static String EXPIRED = "expired";
    private final static String HEADER = "header";
    private final static String BEARER = "bearer";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String)request.getAttribute(EXCEPTION);
        response.setContentType("application/json; charset=UTF-8");

        if(exception!=null) {
            if (exception.equals(EXPIRED)) {
                setResponse(response,HttpStatus.UNAUTHORIZED.value(),"만료된 토큰입니다", 1000);
            } if (exception.equals(MALFORMED)) {
                setResponse(response,HttpStatus.BAD_REQUEST.value(), "잘못된 형식의 토큰입니다");
            } if(exception.equals(HEADER)) {
                setResponse(response,HttpStatus.BAD_REQUEST.value(), "Authorization 헤더가 존재하지 않습니다.");
            } if(exception.equals(LOGOUT)) {
                setResponse(response, HttpStatus.BAD_REQUEST.value(), "로그아웃 처리된 토큰입니다.");
            } if(exception.equals(BEARER)) {
                setResponse(response, HttpStatus.BAD_REQUEST.value(), "Bearer 형식이 잘못됐습니다.");
            }
        }
    }
    public void setResponse(HttpServletResponse response,int status,String msg) throws IOException{
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("status",status);
        json.put("success", false);
        json.put("message", msg);
        String newResponse = new ObjectMapper().writeValueAsString(json);
        response.getWriter().write(newResponse);
        response.setStatus(status);
    }
    public void setResponse(HttpServletResponse response,int status,String msg, int code) throws IOException{
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("status",status);
        json.put("success", false);
        json.put("message", msg);
        json.put("code", code);
        String newResponse = new ObjectMapper().writeValueAsString(json);
        response.getWriter().write(newResponse);
        response.setStatus(status);
    }
}
