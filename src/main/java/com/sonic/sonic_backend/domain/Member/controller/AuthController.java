package com.sonic.sonic_backend.domain.Member.controller;

import com.sonic.sonic_backend.domain.Member.dto.GeneralSignInRequestDto;
import com.sonic.sonic_backend.domain.Member.dto.MailSendRequestDto;
import com.sonic.sonic_backend.domain.Member.dto.ReissueDto;
import com.sonic.sonic_backend.domain.Member.dto.SignUpRequestDto;
import com.sonic.sonic_backend.domain.Member.service.AuthService;
import com.sonic.sonic_backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}
