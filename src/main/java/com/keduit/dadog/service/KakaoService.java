package com.keduit.dadog.service;

import com.keduit.dadog.domain.KakaoMember;
//import com.keduit.dadog.dto.KakaoDTO;
import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.KakaoMemberRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// 카카오 로그인
// 카카오서비스는 로그인하고 로그인한 유저정보를 받아옴.

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoMemberRepository kakaoMemberRepository; // 카카오 회원 정보 저장소
    private final UserService userService; // 사용자 서비스

    // 카카오 API 관련 설정 값들
    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.client.secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    // 카카오 인증 및 API 기본 URL
    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    // 카카오 로그인 URL 생성 메서드
    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    // 카카오 인증 코드로 사용자 정보 조회
    public UserDTO getKakaoInfo(String code) throws Exception {
        if (code == null) throw new Exception("Failed to get authorization code");

        String accessToken = "";
        try {
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            // 요청 파라미터 설정
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_CLIENT_ID);
            params.add("client_secret", KAKAO_CLIENT_SECRET);
            params.add("code", code);
            params.add("redirect_uri", KAKAO_REDIRECT_URL);

            // RestTemplate을 사용하여 카카오 토큰 요청
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            // JSON 파싱하여 액세스 토큰 추출
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            accessToken = (String) jsonObj.get("access_token");

        } catch (Exception e) {
            throw new Exception("API call failed");
        }
        // 액세스 토큰으로 사용자 정보 조회
        return getUserInfoWithToken(accessToken);
    }

    // 액세스 토큰을 사용하여 카카오 사용자 정보 조회
    private UserDTO getUserInfoWithToken(String accessToken) throws Exception {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");

        // RestTemplate을 사용하여 카카오 사용자 정보 요청
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        // JSON 파싱하여 사용자 정보 추출
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

        // 사용자 정보 추출
        String id = String.valueOf(jsonObj.get("id"));
        String email = String.valueOf(account.get("email"));
        String nickname = String.valueOf(profile.get("nickname"));

        // UserDTO 객체 생성
        UserDTO userDTO = UserDTO.builder()
                .kakaoId(id)
                .email(email)
                .nickname(nickname)
                .build();

        // UserService를 통해 사용자 정보 저장 또는 조회
        userService.kakaoLogin(userDTO);

        // 세션에 사용자 정보 저장 (선택적)
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.setAttribute("member", userDTO);

        return userDTO;
    }
}
