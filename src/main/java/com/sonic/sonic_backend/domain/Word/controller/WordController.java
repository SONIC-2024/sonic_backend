package com.sonic.sonic_backend.domain.Word.controller;

import com.sonic.sonic_backend.domain.Word.service.WordService;
import com.sonic.sonic_backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;



import static com.sonic.sonic_backend.response.Message.*;
import static com.sonic.sonic_backend.response.Response.success;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "word")
@RestController
@RequestMapping(name = "/word")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @Operation(summary = "단어 목록 불러오기")
    @ResponseStatus(OK)
    @GetMapping("/list")
    public Response getQuiz1(@RequestParam("category") String c, Pageable pageable) {
        return success(GET_WORD_LIST_SUCCESS, wordService.getList(c, pageable));
    }

}
