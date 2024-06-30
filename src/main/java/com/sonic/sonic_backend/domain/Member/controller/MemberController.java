package com.sonic.sonic_backend.domain.Member.controller;

import com.sonic.sonic_backend.domain.Member.dto.common.SignUpRequestDto;
import com.sonic.sonic_backend.domain.Member.repository.MemberRepository;
import com.sonic.sonic_backend.domain.Member.service.MemberService;
import com.sonic.sonic_backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.sonic.sonic_backend.response.Message.*;
import static com.sonic.sonic_backend.response.Response.success;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Member")
@RestController
@AllArgsConstructor
@RequestMapping(value="/member")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Operation(summary = "회원 이름 불러오기")
    @ResponseStatus(OK)
    @GetMapping
    public Response getMember() {
        return success(GET_MEMBER_NAME_SUCCESS, memberService.getMemberName());
    }

}
