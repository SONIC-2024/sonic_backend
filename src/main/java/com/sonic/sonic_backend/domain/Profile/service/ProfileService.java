package com.sonic.sonic_backend.domain.Profile.service;

import com.sonic.sonic_backend.configuration.AWS.S3Service;
import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.entity.MemberGeneral;
import com.sonic.sonic_backend.domain.Member.repository.MemberGeneralRepository;
import com.sonic.sonic_backend.domain.Member.service.MemberService;
import com.sonic.sonic_backend.domain.Profile.dto.AttendanceResponseDto;
import com.sonic.sonic_backend.domain.Profile.dto.TierResponseDto;
import com.sonic.sonic_backend.domain.Profile.entity.Attendance;
import com.sonic.sonic_backend.domain.Profile.entity.MemberProfile;
import com.sonic.sonic_backend.domain.Profile.entity.WeekAttendance;
import com.sonic.sonic_backend.domain.Profile.repository.RankingRepository;
import com.sonic.sonic_backend.domain.Tier;
import com.sonic.sonic_backend.exception.SocialMemberUpdatePassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberGeneralRepository memberGeneralRepository;
    private final MemberService memberService;
    private final S3Service s3Service;
    private final PasswordEncoder passwordEncoder;
    private final RankingRepository rankingRepository;

    @Transactional(readOnly = true)
    public AttendanceResponseDto getAttendance() {
        Member member = memberService.getCurrentMember();
        WeekAttendance weekAttendance = member.getWeekAttendance();
        Attendance attendance = member.getAttendance();

        return AttendanceResponseDto.toDto(getWeekList(weekAttendance), attendance.getContinuous_attendance());
    }

    @Transactional
    public TierResponseDto getTierInfo() {
        Member member = memberService.getCurrentMember();
        Long score = rankingRepository.getScore(member);
        double top = getTop(score);
        Tier tier = getTier(top);
        updateTier(member, tier);
        return TierResponseDto.toDto(s3Service.getFullUrl(tier.url), tier.name, top, score);
    }
    @Transactional
    public void updateRankingTiers(Member member) {
        double top = getTop(rankingRepository.getScore(member));
        updateTier(member, getTier(top));
    }

    public boolean[] getWeekList(WeekAttendance weekAttendance) {
        return new boolean[] {
        weekAttendance.isMON(), weekAttendance.isTUE(), weekAttendance.isWED()
                , weekAttendance.isTHU(), weekAttendance.isFRI(),weekAttendance.isSAT(), weekAttendance.isSUN() };
    }
    public double getTop(Long score) {
        Long firstRanking = rankingRepository.getRankingById(getFirstIdOfSameScore(score));
        Long allCount = rankingRepository.getAllCount();
        //소수점 첫 째 자리까지 표시
        return Math.round((firstRanking.doubleValue()/allCount.doubleValue()*100*10))/10.0;
    }
    public Tier getTier(double top) {
        if (top <= Tier.GOLD_I.top) return Tier.GOLD_I; //10~ : GOLD1
        else if(Tier.GOLD_II.top>=top && top > Tier.GOLD_I.top) return Tier.GOLD_II; //20~10 : GOLD2
        else if(Tier.GOLD_III.top>=top && top > Tier.GOLD_II.top) return Tier.GOLD_III; //20~30 : COLD3
        else if(Tier.SILVER_I.top>=top && top > Tier.GOLD_III.top) return Tier.SILVER_I; //30~40 : SILVER1
        else if(Tier.SILVER_II.top>=top && top > Tier.SILVER_I.top) return Tier.SILVER_II; //40~50 : SILVER2
        else if(Tier.SILVER_III.top>=top && top > Tier.SILVER_II.top) return Tier.SILVER_III; //50~60 : SILVER3
        else if(Tier.BRONZE_I.top>=top && top > Tier.SILVER_III.top) return Tier.BRONZE_I; //60~70 : BRONZE1
        else if(Tier.BRONZE_II.top>=top && top > Tier.BRONZE_I.top) return Tier.BRONZE_II; //70~80 : BRONZE2
        else if(Tier.BRONZE_III.top>=top && top > Tier.BRONZE_II.top) return Tier.BRONZE_III; //80~90 : BRONZE3
        else return Tier.IRON; // 90~100 : IRON
    }

    private Long getFirstIdOfSameScore(Long score) {
        String id = rankingRepository.getFirstOfSameScore(score);
        // [id] 형태로 반환됨
        return Long.valueOf(id.substring(1, id.length()-1));
    }

    @Transactional
    public int updateExp(int exp) {
        MemberProfile memberProfile = getCurrentMemberProfile();
        memberProfile.addExp(exp);
        return memberProfile.getExp();
    }

    @Transactional
    public void updateProfileImg(MultipartFile file) throws IOException {
        MemberProfile memberProfile = getCurrentMemberProfile();
        String key = s3Service.saveProfileFile(file);

        String previousProfile = memberProfile.getProfileImgUrl();
        memberProfile.updateProfileImgUrl(key);
        if(!previousProfile.equals("profile.jpg")) s3Service.delete(key);
    }

    @Transactional
    public void updateHand(String hand) {
        getCurrentMemberProfile().updateHand(hand);
    }
    @Transactional
    public void updateNickname(String nickname) {
        getCurrentMemberProfile().updateNickname(nickname);
    }
    @Transactional
    public void updatePassword(String password) {
        MemberGeneral memberGeneral = memberGeneralRepository.findByMember(memberService.getCurrentMember());

        if(memberGeneral==null) {throw new SocialMemberUpdatePassword();}
        memberGeneral.updatePassword(passwordEncoder.encode(password));
    }
    @Transactional
    public void updateTier(Member member, Tier tier) {
        member.getMemberProfile().updateTier(tier);
    }

    private MemberProfile getCurrentMemberProfile() {
        Member member = memberService.getCurrentMember();
        return member.getMemberProfile();
    }

}
