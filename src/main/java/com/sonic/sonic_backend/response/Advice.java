package com.sonic.sonic_backend.response;

import com.sonic.sonic_backend.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Advice {

    @ExceptionHandler(DuplicatedEmail.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response DuplicateEmailResponse() {
        return Response.fail(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다");
    }
    @ExceptionHandler(EmailNotValid.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response EmailNotValidResponse() {
        return Response.fail(HttpStatus.CONFLICT, "이메일 인증을 다시 확인해주세요");
    }
}
