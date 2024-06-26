package com.sonic.sonic_backend.domain.Member.controller;

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

}
