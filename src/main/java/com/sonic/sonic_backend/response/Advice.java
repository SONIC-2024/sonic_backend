package com.sonic.sonic_backend.response;

import com.sonic.sonic_backend.exception.*;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(LogOutToken.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response LogOutTokenResponse() {
        return Response.fail(HttpStatus.BAD_REQUEST, "로그아웃된 유효하지 않은 토큰입니다.");
    }

    @ExceptionHandler(LogInNotMatch.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response LogInNotMatchResponse() {
        return Response.fail(HttpStatus.BAD_REQUEST, "이메일과 비밀번호를 다시 확인해주세요.");
    }

    //filter 거친 후 controller 단에서 발생한 MalformedJwtException : reissue 시 발생
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response MalformedTokenResponse() {
        return Response.fail(HttpStatus.BAD_REQUEST, "잘못된 형식의 토큰입니다");
    }

    @ExceptionHandler(SocialMemberUpdatePassword.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response SocialMemberUpdatePasswordResponse() {
        return Response.fail(HttpStatus.BAD_REQUEST, "소셜로그인 회원은 비밀번호를 변경할 수 없습니다.");
    }
    @ExceptionHandler(QuizNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response QuizNotFoundResponse() {
        return Response.fail(HttpStatus.BAD_REQUEST, "퀴즈를 찾을 수 없습니다.");
    }

    @ExceptionHandler(WordNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response WordNotFoundResponse() {
        return Response.fail(HttpStatus.BAD_REQUEST, "단어를 찾을 수 없습니다.");
    }

    @ExceptionHandler(NoAuthAccessToken.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response NoAuthAccessTokenResponse() {return Response.fail(HttpStatus.BAD_REQUEST, "권한정보가 없는 accessToken입니다.");}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response NotValidResponse(MethodArgumentNotValidException ex) {
        String msg = "";
        BindingResult error = ex.getBindingResult();
        if(error.hasErrors()) {
            String code = error.getFieldError().getCode();
            String message = error.getFieldError().getDefaultMessage();
            msg = code+" "+message;
        }
        return Response.fail(HttpStatus.BAD_REQUEST, msg);
    }
    @ExceptionHandler(TypeNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response TypeNotFoundResponse() {return Response.fail(HttpStatus.BAD_REQUEST, "잘못된 category입니다.");}

}
