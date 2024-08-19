package com.sonic.sonic_backend.domain.Member.controller;

import com.sonic.sonic_backend.domain.Member.dto.general.GeneralSignInRequestDto;
import com.sonic.sonic_backend.domain.Member.dto.general.MailSendRequestDto;
import com.sonic.sonic_backend.domain.Member.dto.common.ReissueDto;
import com.sonic.sonic_backend.domain.Member.dto.common.SignUpRequestDto;
import com.sonic.sonic_backend.domain.Member.service.AuthService;
import com.sonic.sonic_backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.sonic.sonic_backend.response.Message.*;
import static org.springframework.http.HttpStatus.OK;
import static com.sonic.sonic_backend.response.Response.*;

@Tag(name = "Auth")
@RestController
@AllArgsConstructor
@RequestMapping(value="/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입")
    @ResponseStatus(OK)
    @PostMapping("/sign-up")
    public Response signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        return success(SIGN_UP_SUCCESS);
    }

    @Operation(summary = "일반 로그인")
    @ResponseStatus(OK)
    @PostMapping("/sign-in/general")
    public Response signInGeneral(@RequestBody GeneralSignInRequestDto generalSignInRequestDto) {
        return success(SIGN_IN_SUCCESS,authService.signInGeneral(generalSignInRequestDto));
    }

    @Operation(summary = "카카오 로그인")
    @ResponseStatus(OK)
    @GetMapping("/sign-in/kakao")
    public Response signInKakao(@RequestParam("code") String authCode) throws IOException {
        return success(SIGN_IN_SUCCESS,authService.signInKakao(authCode));
    }

    @Operation(summary = "액세스 토큰 재발급")
    @ResponseStatus(OK)
    @PostMapping("/reissue")
    public Response reIssue(@RequestBody ReissueDto reIssueDto) {
        return success(REISSUE_SUCCESS, authService.reissue(reIssueDto));
    }

    @Operation(summary = "인증 이메일 보내기")
    @ResponseStatus(OK)
    @PostMapping("/send-mail")
    public Response reIssue(@RequestBody MailSendRequestDto mailSendRequestDto) {
        authService.sendMail(mailSendRequestDto);
        return success(SEND_EMAIL_SUCCESS);
    }

    @Operation(summary = "비밀번호 찾기")
    @ResponseStatus(OK)
    @PatchMapping("/password")
    public Response findPassword(@RequestBody MailSendRequestDto mailSendRequestDto) {
        authService.findPassword(mailSendRequestDto);
        return success(SEND_EMAIL_SUCCESS);
    }

    @Operation(summary = "로그아웃")
    @ResponseStatus(OK)
    @PostMapping("/log-out")
    public Response logOut(@RequestHeader("Authorization") String accessToken) {
        authService.logOut(accessToken);
        return success(LOG_OUT_SUCCESS);
    }


}
