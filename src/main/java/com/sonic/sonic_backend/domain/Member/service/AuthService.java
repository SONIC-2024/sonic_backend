package com.sonic.sonic_backend.domain.Member.service;

import com.sonic.sonic_backend.configuration.AWS.S3Service;
import com.sonic.sonic_backend.configuration.Auth.JwtProvider;
import com.sonic.sonic_backend.domain.Member.dto.common.ReissueDto;
import com.sonic.sonic_backend.domain.Member.dto.common.TokenDto;
import com.sonic.sonic_backend.domain.Member.dto.general.GeneralSignInRequestDto;
import com.sonic.sonic_backend.domain.Member.dto.general.MailSendRequestDto;
import com.sonic.sonic_backend.domain.Member.dto.common.SignUpRequestDto;
import com.sonic.sonic_backend.domain.Member.dto.social.KakaoUserInfoResponseDto;
import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.entity.RefreshToken;
import com.sonic.sonic_backend.domain.Member.repository.*;
import com.sonic.sonic_backend.domain.Profile.entity.Attendance;
import com.sonic.sonic_backend.domain.Profile.entity.MemberProfile;
import com.sonic.sonic_backend.domain.Profile.entity.WeekAttendance;
import com.sonic.sonic_backend.domain.Profile.repository.AttendanceRepository;
import com.sonic.sonic_backend.domain.Profile.repository.MemberProfileRepository;
import com.sonic.sonic_backend.domain.Profile.repository.RankingRepository;
import com.sonic.sonic_backend.domain.Profile.repository.WeekAttendanceRepository;
import com.sonic.sonic_backend.exception.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AttendanceRepository attendanceRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final WeekAttendanceRepository weekAttendanceRepository;
    private final MemberGeneralRepository memberGeneralRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService;
    private final AuthCodeRepository authCodeRepository;
    private final KakaoService kakaoService;
    private final MemberSocialRepository memberSocialRepository;
    private final MemberService memberService;
    private final RankingRepository rankingRepository;
    private final S3Service s3Service;


    @Transactional
    public void sendMail(MailSendRequestDto mailSendRequestDto) {
        String email = mailSendRequestDto.getEmail();
        emailService.joinEmail(email);
    }

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        String email = signUpRequestDto.getEmail();
        //중복이메일검증
        checkIfDuplicated(email);
        //유효이메일검증
        checkValidEmail(signUpRequestDto.getEmail(), signUpRequestDto.getEmailCode());
        saveMember(signUpRequestDto, true);
    }

    @Transactional
    public TokenDto signInGeneral(GeneralSignInRequestDto generalSignInRequestDto) {
        // 1. UsernamePasswordAuthenticationToken 기반 authenticate() 실행 -> Authentication 얻기
        Authentication authentication = setAuthentication(generalSignInRequestDto.getEmail(), generalSignInRequestDto.getPassword());
        System.out.println("done setting authentication");
        // 2. authentication 인자로 넘겨 토큰생성
        TokenDto tokenDto = jwtProvider.generateToken(authentication);
        System.out.println("done generating token");
        // 3. refresh token 저장
        saveRefreshToken(tokenDto.getRefreshToken(), generalSignInRequestDto.getEmail());
        // 4. attendance 갱신
        memberService.updateAttendance();
        return tokenDto;
    }

    @Transactional
    public TokenDto signInKakao(String authCode) {
        KakaoUserInfoResponseDto userInfo = kakaoService.getUSerInfo(kakaoService.getAccessToken(authCode));
        String emailButId = userInfo.getId().toString();

        //신규회원 -> 회원가입 : socialId를 email로 사용
        if(!memberRepository.existsByEmail(emailButId)) saveMember(getSignUpRequestDto(userInfo), false);

        /* createUserDetails에서 password를 provider로 설정함
        email : memberSocial의 socialId 사용 */
        Authentication authentication = setAuthentication(emailButId, "KAKAO");
        System.out.println("done setting authentication");
        // 2. authentication 인자로 넘겨 토큰생성
        TokenDto tokenDto = jwtProvider.generateToken(authentication);
        System.out.println("done generating token");
        // 3. refresh token 저장
        saveRefreshToken(tokenDto.getRefreshToken(), emailButId);
        // 4. attendance 갱신
        memberService.updateAttendance();
        return tokenDto;
    }


    public ReissueDto reissue(ReissueDto reIssueDto) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(reIssueDto.getRefreshToken());
        //1. 리프레시토큰 검증
        RefreshToken refreshToken = validateRefreshToken(reIssueDto, refreshTokenOptional);
        //2. 토큰 재발급
        Authentication authentication = jwtProvider.getAuthentication(reIssueDto.getAccessToken());
        TokenDto tokenDto = jwtProvider.generateToken(authentication);
        //3. 기존 리프레시토큰 삭제, 새 리프레시토큰 redis에 저장
        refreshTokenRepository.deleteByRefreshToken(refreshToken.getRefreshToken());
        saveRefreshTokenReissue(tokenDto.getRefreshToken(), refreshToken.getMemberId());
        return ReissueDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
    }

    public void logOut(String header) {
        System.out.println("header : "+header);
        String accessToken = resolveToken(header).orElseThrow(()->new RuntimeException("토큰이 비어있습니다."));
        Member member = memberService.getCurrentMember();
        //액세스토큰 블랙리스트 등록
        refreshTokenRepository.saveBlackList(accessToken);
        //기존 리프레시토큰 삭제
        RefreshToken refreshToken = refreshTokenRepository.findById(member.getId().toString())
                .orElseThrow(()-> new RuntimeException("이미 삭제된 토큰입니다"));
        refreshTokenRepository.deleteByRefreshToken(refreshToken.getRefreshToken());
    }

    @Transactional
    public void findPassword(MailSendRequestDto mailSendRequestDto) {
        // 가입된 이메일 존재하는지 확인
        String email = mailSendRequestDto.getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFound::new);
        // 메일보내기
        String newPassword = generateRandomString();
        emailService.joinEmail(email, newPassword);
        // 바뀐 패스워드 저장
        memberGeneralRepository.findByMember(member).updatePassword(passwordEncoder.encode(newPassword));
    }

    public String generateRandomString() {
        int length = 8;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    private RefreshToken validateRefreshToken(ReissueDto reIssueDto, Optional<RefreshToken> refreshTokenOptional) {
        if(refreshTokenOptional.isEmpty()) throw new RefreshTokenExpired();
        RefreshToken refreshToken = refreshTokenOptional.get();
        if(!refreshToken.getRefreshToken().equals(reIssueDto.getRefreshToken()))
            throw new RuntimeException("토큰의 유저정보가 일치하지 않습니다.");
        return refreshToken;
    }


    @Transactional
    private void saveMember(SignUpRequestDto signUpRequestDto, boolean isGeneral) {
        MemberProfile memberProfile = memberProfileRepository.save(
                signUpRequestDto.toMemberProfileEntity(signUpRequestDto, s3Service.getFullUrl("profile.jpg")));
        Attendance attendance = attendanceRepository.save(getInitialAttendance());
        WeekAttendance weekAttendance = weekAttendanceRepository.save(getEmptyWeekAttendance());
        Member member = signUpRequestDto.toMemberEntity(signUpRequestDto.getEmail(),memberProfile,attendance,weekAttendance);
        memberRepository.save(member);
        //일반, 소셜로그인 분기
        if(isGeneral) {
            memberGeneralRepository.save(signUpRequestDto
                    .toMemberGeneralEntity(member,passwordEncoder.encode(signUpRequestDto.getPassword())));
        } else {
            memberSocialRepository.save(signUpRequestDto
                    .toMemberSocialEntity(member, Long.valueOf(signUpRequestDto.getEmail())));
        }
        rankingRepository.addOrUpdate(
                memberRepository.findByEmail(member.getEmail()).orElseThrow(MemberNotFound::new).getId().toString()
                , 0);
    }

    //로그인 시 리프레시 토큰 저장
    private void saveRefreshToken(String tokenValue, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFound::new);
        RefreshToken refreshToken = new RefreshToken(tokenValue, member.getId().toString());
        System.out.println("refresh token done");
        refreshTokenRepository.save(refreshToken);
    }
    //reissue 시 리프레시 토큰 저장
    private void saveRefreshTokenReissue(String tokenValue, String id) {
        RefreshToken refreshToken = new RefreshToken(tokenValue,id);
        System.out.println("refresh token done");
        refreshTokenRepository.save(refreshToken);
    }

    private Authentication setAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuth = new UsernamePasswordAuthenticationToken(email, password);
        System.out.println("done getting UsernamePasswordAuthenticationToken");
        //customUserDetails -> loadUserByUsername 호출 : 실제 검증 수행
        System.out.println(usernamePasswordAuth.getName()+", "+usernamePasswordAuth.getCredentials());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuth);
        System.out.println("getting context");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }



    private void checkIfDuplicated(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new DuplicatedEmail();
        }
    }
    private void checkValidEmail(String email, int authCode) {
        Optional<Integer> foundAuthCodeOptional = authCodeRepository.findById(email);
        if(foundAuthCodeOptional.isEmpty()) throw new EmailNotValid();
        int foundAuthCode = foundAuthCodeOptional.get();
        if(foundAuthCode!=authCode) throw new EmailNotValid();
    }

    private SignUpRequestDto getSignUpRequestDto(KakaoUserInfoResponseDto userInfo) {
        return SignUpRequestDto.builder()
                .email(userInfo.getId().toString())
                .nickname(userInfo.getKakaoAccount().getProfile().getNickName())
                .hand("right")
                .build();
    }

    private WeekAttendance getEmptyWeekAttendance() {
        return WeekAttendance.builder()
                .MON(false).TUE(false).WED(false).THU(false).FRI(false).SAT(false).SUN(false).build();
    }

    private Attendance getInitialAttendance() {
        return Attendance.builder()
                .last_date(LocalDate.now())
                .continuous_attendance(0).max_continuous_attendance(0)
                .build();
    }

    private Optional<String> resolveToken(String header) {
        if (StringUtils.hasText(header) && header.startsWith("Bearer")) {
            return Optional.of(header.substring(7));
        }
        return Optional.empty();
    }
}
