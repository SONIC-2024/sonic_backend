package com.sonic.sonic_backend.domain.Profile.controller;

import com.sonic.sonic_backend.domain.Member.service.MemberService;
import com.sonic.sonic_backend.domain.Profile.service.ProfileService;
import com.sonic.sonic_backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.sonic.sonic_backend.response.Message.*;
import static com.sonic.sonic_backend.response.Response.success;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Learning")
@RestController
@RequestMapping(name = "/learning")
@RequiredArgsConstructor
public class learningController {

    private final ProfileService profileService;

    @Operation(summary = "출석 조회")
    @ResponseStatus(OK)
    @GetMapping("/attendance")
    public Response getAttendance() {
        return success(GET_ATTENDANCE_SUCCESS, profileService.getAttendance());
    }

    @Operation(summary = "티어 조회")
    @ResponseStatus(OK)
    @GetMapping("/tier")
    public Response getTier() { return success(GET_TIER_SUCCESS, profileService.getTier()); }
}
