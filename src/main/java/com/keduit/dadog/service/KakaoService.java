package com.keduit.dadog.service;

import com.keduit.dadog.domain.KakaoMember;
//import com.keduit.dadog.dto.KakaoDTO;
import com.keduit.dadog.dto.UserDTO;
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

    private final KakaoMemberRepository kakaoMemberRepository; // 의존성 주입.
    private final UserService userService; // UserService 주입

    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.client.secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    // 카카오 로그인 URL 생성
    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    // 카카오 정보 가져오기
    public UserDTO getKakaoInfo(String code) throws Exception {
        if (code == null) throw new Exception("Failed to get authorization code");

        String accessToken = "";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_CLIENT_ID);
            params.add("client_secret", KAKAO_CLIENT_SECRET);
            params.add("code", code);
            params.add("redirect_uri", KAKAO_REDIRECT_URL);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            accessToken = (String) jsonObj.get("access_token");

        } catch (Exception e) {
            throw new Exception("API call failed");
        }
        return getUserInfoWithToken(accessToken);
    }

    private UserDTO getUserInfoWithToken(String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

        // Long 타입의 id를 String으로 변환
        String id = String.valueOf(jsonObj.get("id"));
        String email = String.valueOf(account.get("email"));
        String nickname = String.valueOf(profile.get("nickname"));

        UserDTO userDTO = UserDTO.builder()
                .kakaoId(id)
                .email(email)
                .nickname(nickname)
                .build();

        // UserService를 통해 사용자 정보를 저장하거나 조회
        userService.kakaoLogin(userDTO);

        // 세션에 사용자 정보 저장 (optional)
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.setAttribute("member", userDTO);

        return userDTO;
    }


}
