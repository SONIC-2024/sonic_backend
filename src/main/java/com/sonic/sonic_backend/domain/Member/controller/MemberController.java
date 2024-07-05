package com.sonic.sonic_backend.domain.Member.controller;

import com.sonic.sonic_backend.domain.Member.dto.common.SignUpRequestDto;
import com.sonic.sonic_backend.domain.Member.repository.MemberRepository;
import com.sonic.sonic_backend.domain.Member.service.MemberService;
import com.sonic.sonic_backend.domain.Profile.dto.UpdateProfileRequestDto;
import com.sonic.sonic_backend.domain.Profile.service.ProfileService;
import com.sonic.sonic_backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.sonic.sonic_backend.response.Message.*;
import static com.sonic.sonic_backend.response.Response.success;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Member")
@RestController
@AllArgsConstructor
@RequestMapping(value="/member")
public class MemberController {

    private final MemberService memberService;
    private final ProfileService profileService;

    @Operation(summary = "회원 이름 불러오기")
    @ResponseStatus(OK)
    @GetMapping
    public Response getMember() {
        return success(GET_MEMBER_NAME_SUCCESS, memberService.getMemberName());
    }
    @Operation(summary = "회원 탈퇴하기")
    @ResponseStatus(OK)
    @DeleteMapping
    public Response deleteMember() {
        memberService.deleteMember();
        return success(DELETE_MEMBER_SUCCESS);
    }

    @Operation(summary = "비밀번호 변경")
    @ResponseStatus(OK)
    @PatchMapping("/password")
    public Response updatePassword(@RequestBody @Valid UpdateProfileRequestDto updateProfileRequestDto) {
        profileService.updatePassword(updateProfileRequestDto.getPassword());
        return success(DELETE_MEMBER_SUCCESS);
    }

    @Operation(summary = "닉네임 변경")
    @ResponseStatus(OK)
    @PatchMapping("/nickname")
    public Response updateNickname(@RequestBody @Valid UpdateProfileRequestDto updateProfileRequestDto) {
        profileService.updateNickname(updateProfileRequestDto.getNickname());
        return success(DELETE_MEMBER_SUCCESS);
    }

    @Operation(summary = "손 변경")
    @ResponseStatus(OK)
    @PatchMapping("/hand")
    public Response updateHand(@RequestBody @Valid UpdateProfileRequestDto updateProfileRequestDto) {
        profileService.updateHand(updateProfileRequestDto.getHand());
        return success(UPDATE_HAND_SUCCESS);
    }



    @Operation(summary = "프로필사진 변경")
    @ResponseStatus(OK)
    @PatchMapping(value="/profile-img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response updateProfileImg(@RequestParam("file")MultipartFile file) throws IOException {
        profileService.updateProfileImg(file);
        return success(UPDATE_PROFILE_IMG_SUCCESS);
    }


    @Operation(summary = "테스트용")
    @ResponseStatus(OK)
    @PatchMapping("/exp")
    public Response updateExp(@RequestBody UpdateProfileRequestDto updateProfileRequestDto) {
        return success("exp 수정 성공", profileService.updateExp(updateProfileRequestDto.getExp()));
    }

}
