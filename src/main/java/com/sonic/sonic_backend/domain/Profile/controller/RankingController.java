package com.sonic.sonic_backend.domain.Profile.controller;

import com.sonic.sonic_backend.domain.Profile.repository.MemberProfileRepository;
import com.sonic.sonic_backend.domain.Profile.service.RankingService;
import com.sonic.sonic_backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static com.sonic.sonic_backend.response.Response.*;
import static com.sonic.sonic_backend.response.Message.*;

@RestController
@RequestMapping(value = "/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "랭킹 조회")
    @ResponseStatus(OK)
    @GetMapping
    public Response getRanking() {
        return success(GET_RANKING_SUCCESS, rankingService.getRanking());
    }

}
