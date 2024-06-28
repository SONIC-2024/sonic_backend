package com.sonic.sonic_backend.configuration.Auth;

import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.entity.MemberGeneral;
import com.sonic.sonic_backend.domain.Member.repository.MemberGeneralRepository;
import com.sonic.sonic_backend.domain.Member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("in loadUserByUsername");
        Member member =  memberRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다"));

        return createUserDetails(memberGeneralRepository.findByMember(member), member);
    }

    public UserDetails createUserDetails(MemberGeneral memberGeneral, Member member) {
        System.out.println("in createUserDetails");
        System.out.println(member.getEmail()+" "+memberGeneral.getPassword() );
        UserDetails userDetails =  User.builder()
                .username(member.getEmail())
                .password(memberGeneral.getPassword())
                .authorities(new SimpleGrantedAuthority(member.getRole().toString()))
                .build();


        System.out.println(userDetails.getUsername()+" ### "+ userDetails.getAuthorities());
        return userDetails;
    }
}
