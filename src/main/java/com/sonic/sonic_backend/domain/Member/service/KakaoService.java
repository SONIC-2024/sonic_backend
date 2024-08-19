package com.sonic.sonic_backend.domain.Member.service;

import com.sonic.sonic_backend.domain.Member.dto.social.KakaoTokenResponseDto;
import com.sonic.sonic_backend.domain.Member.dto.social.KakaoUserInfoResponseDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KakaoService {

    private static final String KAKAO_OAUTH_URL = "https://kauth.kakao.com";
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com";
    @Value("${kakao.redirect.uri}") private String REDIRECT_URI;
    @Value("${kakao.client.id}") private String clientId;


    //1. 인증코드 -> 액세스토큰 얻기
    public String getAccessToken(String authCode) {
        System.out.println("in getAccessToken");
        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAKAO_OAUTH_URL).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("redirect_uri", REDIRECT_URI)
                        .queryParam("code", authCode)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //Exception 처리
                //.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                //동기식으로 response의 body 가져와서 dto에 매핑
                .bodyToMono(KakaoTokenResponseDto.class).block();
        System.out.println("done getting accessToken");
        return kakaoTokenResponseDto.getAccessToken();
    }

    //액세스토큰 -> 사용자정보 얻기
    public KakaoUserInfoResponseDto getUSerInfo(String accessToken) {
        System.out.println(accessToken);
        KakaoUserInfoResponseDto userInfo = WebClient.create(KAKAO_USER_INFO_URL).get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .queryParam("property_keys", "[\"kakao_account.profile\"]")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDto.class).block();
        return userInfo;
    }

}
