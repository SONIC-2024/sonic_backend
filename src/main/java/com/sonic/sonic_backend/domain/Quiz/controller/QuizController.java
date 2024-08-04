package com.sonic.sonic_backend.domain.Quiz.controller;

import com.sonic.sonic_backend.domain.Quiz.service.QuizService;
import com.sonic.sonic_backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;



import static com.sonic.sonic_backend.response.Message.*;
import static com.sonic.sonic_backend.response.Response.success;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "quiz")
@RestController
@RequestMapping(name = "/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "퀴즈 불러오기 level1")
    @ResponseStatus(OK)
    @GetMapping("/level-1")
    public Response getQuiz1(@RequestParam("quiz-id") Long id  ) {
        return success(GET_QUIZ_1_SUCCESS, quizService.getLevel1(id));
    }

    @Operation(summary = "퀴즈 불러오기 level2")
    @ResponseStatus(OK)
    @GetMapping("/level-2")
    public Response getQuiz2(@RequestParam("quiz-id") Long id  ) {
        return success(GET_QUIZ_2_SUCCESS, quizService.getLevel2(id));
    }

    @Operation(summary = "퀴즈 불러오기 level3")
    @ResponseStatus(OK)
    @GetMapping("/level-3")
    public Response getQuiz3(@RequestParam("quiz-id") Long id  ) {
        return success(GET_QUIZ_3_SUCCESS, quizService.getLevel3(id));
    }

    @Operation(summary = "퀴즈 정답처리")
    @ResponseStatus(OK)
    @PostMapping
    public Response solveQuiz(@RequestParam("quiz-id") Long id) {
        quizService.solveQuiz(id);
        return success(SOLVE_QUIZ_SUCCESS);
    }

    @Operation(summary = "퀴즈 즐겨찾기")
    @ResponseStatus(OK)
    @PostMapping("/star")
    public Response starQuiz(@RequestParam("quiz-id") Long id) {
        quizService.starQuiz(id);
        return success(STAR_QUIZ_SUCCESS);
    }

    @Operation(summary = "퀴즈 불러오기 level1")
    @ResponseStatus(OK)
    @GetMapping("/star/level-1")
    public Response getStarredQuiz1(Pageable pageable) {
        return success(GET_QUIZ_1_SUCCESS, quizService.getStarredLevel1(pageable));
    }
    @Operation(summary = "퀴즈 불러오기 level2")
    @ResponseStatus(OK)
    @GetMapping("/star/level-2")
    public Response getStarredQuiz12(Pageable pageable) {
        return success(GET_QUIZ_1_SUCCESS, quizService.getStarredLevel2(pageable));
    }
    @Operation(summary = "퀴즈 불러오기 level3")
    @ResponseStatus(OK)
    @GetMapping("/star/level-3")
    public Response getStarredQuiz3(Pageable pageable) {
        return success(GET_QUIZ_1_SUCCESS, quizService.getStarredLevel3(pageable));
    }



}
