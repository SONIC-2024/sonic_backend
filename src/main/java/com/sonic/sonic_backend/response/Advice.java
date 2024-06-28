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
    public Response EmailNotValidResponse() {return Response.fail(HttpStatus.CONFLICT, "이메일 인증을 다시 확인해주세요");}

    @ExceptionHandler(MemberNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response UserNotFoundResponse() {return Response.fail(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다");}

    @ExceptionHandler(RefreshTokenExpired.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response RefreshTokenExpiredResponse() {
        return Response.fail(HttpStatus.UNAUTHORIZED, "refresh token이 만료되었습니다.");
    }

    @ExceptionHandler(WrongEmailAddress.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response WrongEmailAddressResponse() {
        return Response.fail(HttpStatus.BAD_REQUEST, "이메일 전송에 실패했습니다. 이메일주소를 확인해주세요.");
    }
}
