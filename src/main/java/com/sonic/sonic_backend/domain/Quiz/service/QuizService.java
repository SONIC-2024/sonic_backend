package com.sonic.sonic_backend.domain.Quiz.service;

import com.sonic.sonic_backend.configuration.AWS.S3Service;
import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.service.MemberService;
import com.sonic.sonic_backend.domain.Profile.repository.RankingRepository;
import com.sonic.sonic_backend.domain.Quiz.dto.*;
import com.sonic.sonic_backend.domain.Quiz.entity.Quiz;
import com.sonic.sonic_backend.domain.Quiz.entity.SolvedQuiz;
import com.sonic.sonic_backend.domain.Quiz.entity.StarredQuiz;
import com.sonic.sonic_backend.domain.Quiz.repository.QuizRepository;
import com.sonic.sonic_backend.domain.Quiz.repository.SolvedQuizRepository;
import com.sonic.sonic_backend.domain.Quiz.repository.StarredQuizRepository;
import com.sonic.sonic_backend.exception.QuizNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {
    char[] fist = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ','ㅆ', 'ㅇ' , 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
    char[] mid = {'ㅏ', 'ㅐ', 'ㅑ','ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'};
    char[] last = {' ', 'ㄱ','ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ','ㄻ', 'ㄼ', 'ㄽ', 'ㄾ','ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
    String[] candidateChoiceList = {"ㄱ","ㄴ","ㄷ","ㄹ","ㅁ","ㅂ","ㅅ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"
            ,"ㅏ","ㅑ","ㅓ","ㅕ","ㅜ","ㅠ","ㅡ","ㅣ","ㅐ","ㅒ","ㅔ","ㅖ","ㅚ","ㅟ","ㅢ"};

    private final QuizRepository quizRepository;
    private final SolvedQuizRepository solvedQuizRepository;
    private final RankingRepository rankingRepository;
    private final StarredQuizRepository starredQuizRepository;
    private final S3Service s3Service;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public QuizLevel1ResponseDto getLevel1(Long id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(QuizNotFound::new);
        String content = quiz.getContent();
        return QuizLevel1ResponseDto.toDto(content, getDetailedContent(content), getIdList(quiz)
                , getIsStarred(id));
    }
    @Transactional(readOnly = true)
    public QuizLevel2ResponseDto getLevel2(Long id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(QuizNotFound::new);
        String content = quiz.getContent();
        return QuizLevel2ResponseDto.toDto(content, getCandidateList(content), s3Service.getFullUrl(quiz.getDetailedContent())
                , getIsStarred(id));
    }

    @Transactional(readOnly = true)
    public QuizLevel3ResponseDto getLevel3(Long id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(QuizNotFound::new);
        return QuizLevel3ResponseDto.toDto(Long.valueOf(quiz.getDetailedContent()), quiz.getContent()
                ,getIsStarred(id));
    }

    private boolean getIsStarred(Long id) {
        return starredQuizRepository.existsByMemberAndQuizId(memberService.getCurrentMember(), id);
    }

    public Page<StarredQuiz1Or3ResponseDto> getStarredLevel1(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        return starredQuizRepository.findLevel1(member.getId(), pageable);
    }
    public Page<StarredQuiz1Or3ResponseDto> getStarredLevel3(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        return starredQuizRepository.findLevel3(member.getId(), pageable);
    }
    public Page<StarredQuiz2ResponseDto> getStarredLevel2(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        return starredQuizRepository.findLevel2(member.getId(), pageable);
    }
    public SolvedQuizNumberResponseDto getSolvedQuizNumbers() {
        Member member = memberService.getCurrentMember();
        return solvedQuizRepository.getSolvedQuizNumbers(member.getId());
    }


    @Transactional
    public void solveQuiz(Long id) {
        Member member = memberService.getCurrentMember();
        SolvedQuiz solvedQuiz = solvedQuizRepository.save(getSolvedQuiz(id, member));
        rankingRepository.increaseScore(member.getId().toString(),3);
    }

    @Transactional
    public void starQuiz(Long id) {
        Member member = memberService.getCurrentMember();
        starOrUnStar(member, id,findStarredQuiz(member,id));
    }

    public void starOrUnStar(Member member, Long quizId, Optional<StarredQuiz> starredQuiz) {
        if(starredQuiz.isEmpty()) {
            star(member, quizId);
        } else {
            unstar(starredQuiz.get());
        }
    }

    public Optional<StarredQuiz> findStarredQuiz(Member member, Long id) {
        return starredQuizRepository.findByMemberAndQuizId(member,id);
    }

    public void star(Member member, Long quizId) {
        starredQuizRepository.save(StarredQuiz.builder()
                        .member(member)
                        .quiz(quizRepository.findById(quizId).orElseThrow(QuizNotFound::new))
                        .build());
    }
    public void unstar(StarredQuiz quiz) {
        starredQuizRepository.delete(quiz);
    }

    private SolvedQuiz getSolvedQuiz(Long id, Member member) {
        return SolvedQuiz.builder()
                .member(member)
                .quiz(quizRepository.findById(id).orElseThrow(QuizNotFound::new))
                .build();
    }

    public ArrayList<Character> getDetailedContent(String content) {
        ArrayList<Character> result = new ArrayList<>();
        for (int i = 0; i < content.length(); i++) {
            int base = content.charAt(i)-44032;
            result.add(fist[base/28/21]);
            result.add(mid[base/28%21]);
            if(base%28!=0) result.add(last[base%28]);
        }
        return result;
    }
    public long[] getIdList(Quiz quiz) {
        return Arrays.stream(quiz.getDetailedContent().split(",")).mapToLong(Long::parseLong).toArray();
    }

    public String[] getCandidateList(String content) {
        String[] candidateList = new String[4];
        candidateList[(int)(Math.random()*5)] = content;
        for (int i = 0; i < 4; i++) {
            if(candidateList[i]==null) {
                candidateList[i] = candidateChoiceList[(int)(Math.random()*29+1)];
            }
        }
        return candidateList;
    }


}
