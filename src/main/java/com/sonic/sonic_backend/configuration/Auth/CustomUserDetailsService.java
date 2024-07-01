package com.sonic.sonic_backend.configuration.Auth;

import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.entity.MemberGeneral;
import com.sonic.sonic_backend.domain.Member.entity.MemberSocial;
import com.sonic.sonic_backend.domain.Member.repository.MemberGeneralRepository;
import com.sonic.sonic_backend.domain.Member.repository.MemberRepository;
import com.sonic.sonic_backend.domain.Member.repository.MemberSocialRepository;
import com.sonic.sonic_backend.exception.LogInNotMatch;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberGeneralRepository memberGeneralRepository;
    private final MemberSocialRepository memberSocialRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("in loadUserByUsername");
        Member member =  memberRepository.findByEmail(email)
                .orElseThrow(LogInNotMatch::new);

        //소셜 or 일반 로그인 구별
        if(!email.contains("@")) {
            return createUserDetails(memberSocialRepository.findByMember(member), member);
        }
        return createUserDetails(memberGeneralRepository.findByMember(member), member);
    }

    public UserDetails createUserDetails(MemberGeneral memberGeneral, Member member) {
        System.out.println("in createUserDetails general");
        System.out.println(member.getEmail()+" "+memberGeneral.getPassword() );
        UserDetails userDetails =  User.builder()
                .username(member.getEmail())
                .password(memberGeneral.getPassword())
                .authorities(new SimpleGrantedAuthority(member.getRole().toString()))
                .build();
        System.out.println(userDetails.getUsername()+" ### "+ userDetails.getAuthorities());
        return userDetails;
    }
    public UserDetails createUserDetails(MemberSocial memberSocial, Member member) {
        System.out.println("in createUserDetails social");
        System.out.println(memberSocial.getSocialId()+" "+memberSocial.getProvider() );
        UserDetails userDetails =  User.builder()
                .username(member.getEmail())
                .password(passwordEncoder.encode(memberSocial.getProvider()))
                .authorities(new SimpleGrantedAuthority(member.getRole().toString()))
                .build();
        System.out.println(userDetails.getUsername()+" ### "+ userDetails.getAuthorities());
        return userDetails;
    }
}
