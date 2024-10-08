package com.keduit.dadog.service;

import com.keduit.dadog.domain.KakaoMember;
import com.keduit.dadog.dto.KakaoDTO;
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

@Service // 해당 클래스가 spring의 서비스 계층에 속함을 나타냄.
@RequiredArgsConstructor //의존성 주입.
public class KakaoService {

    @Autowired
    private final KakaoMemberRepository kakaoMemberRepository; // 의존성 주입.
    // 앱키 받아옴.
    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.client.secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com"; // 카카오 계정 인증을 위한 URI

    private final static String KAKAO_API_URI = "https://kapi.kakao.com"; // 카카오 API에 접근할 때 사용되는 URI



    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize" // "/oauth/authorize" => 사용자에게 권한 부여요청. 사용자가 해당 요청을 수락하면 권한 부여 코드를 발급하는 엔드포인트.
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    public KakaoDTO getKakaoInfo(String code) throws Exception {
        if (code == null) throw new Exception("Failed to get authorization code");
        // 코드가 null이면 인증받지 못한것이기 때문에 예외 발생.

        String accessToken = "";
        // try-catch문을 사용하여 예외가 발생하면 프로그램이 강제로 종료되는것을 방지하고 사용자에게 의미 있는 오류 메시지를 전달할 수 있도록 try-catch문 사용.
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");
            //HTTP 요청에서 사용되는 콘텐츠 유형(Content-Type) : 웹 폼 데이터를 서버로 전송하는 데 주로 사용

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            // 카카오 API와 통신을 위해 필요한 파라미터. 통신을 위해 필요한 파라미터들을 멀티 밸류맵에 넣어줬음.
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_CLIENT_ID);
            params.add("client_secret", KAKAO_CLIENT_SECRET);
            params.add("code", code);
            params.add("redirect_uri", KAKAO_REDIRECT_URL);



            RestTemplate restTemplate = new RestTemplate(); //RestTemplate은 스프링에서 지원하는 객체로 간편하게 레스트 방식API 호출할수 있는 스프링클래스.
            // 헤더나 콘텐츠 타입들을 설정하여 외부API를 호출. 서버 투 서버 통신에 사용.
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers); //http 요청에 필요한 헤더와 바디 생성.

            // RestTemplate을 사용하여 서버로 HTTP POST 요청을 보냄.
            // exchange()메소드로 api 호출
            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class // 응답 형식
            );
            // JSON 형식의 문자열을 Java 객체로 변환하기위해 제이슨파서 사용.
            JSONParser jsonParser = new JSONParser();
            // HTTP 응답에서 JSON 문자열을 가져와 파싱하고 제이슨오브젝트로 형변환.
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            accessToken = (String) jsonObj.get("access_token"); // accessToken을 추출하여 토큰 변수에 할당해줬음.

        } catch (Exception e) {
            throw new Exception("API call failed");
        }
        return getUserInfoWithToken(accessToken);
    }

    private KakaoDTO getUserInfoWithToken(String accessToken) throws Exception {
        //HTTPHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken); // 컨텐츠 타입 헤더를 추가함. Authorization=> 인증토큰 포함
                                                                                     // Bearer 2.0 프로토콜을 사용하여 보호된 리소스에 접근할때 사용됨.
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");

        // HttpHeader 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);  //http헤더에 담아줌.
        ResponseEntity<String> response = rt.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        //Response 데이터 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        // 카카오 어카운트에 이메일이 저장되어있고 프로필에 닉네임이 저장되어 있어서, 카카오 어카운트에 해당하는 키값을 어카운트 변수에 넣어줌.
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

        long id = (long) jsonObj.get("id"); // 사용자의 고유 ID는 "id" 키에서 추출
        String email = String.valueOf(account.get("email")); // 어카운트에서 이메일에 해당하는 값을 이메일로 뽑아옴.
        String nickname = String.valueOf(profile.get("nickname"));  // 프로필에서 닉네임에 해당하는 값을 뽑아옴.

        KakaoMember kakaoMember = new KakaoMember();
        // 멤버 객체에 이메일과 닉네임을 설정
        // 데이터베이스에 저장해줬음.
        kakaoMember.setEmail(email);
        kakaoMember.setUsername(nickname);
        kakaoMemberRepository.save(kakaoMember);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        // RequestContextHolder.currentRequestAttributes() : 현재 스레드에 바인딩된 요청 속성을 반환.
        // 리퀘스트 어트리뷰트 객체를 반환하기 때문에 서블리 어트리뷰트를 사용하여서 해당 타입으로 캐스팅을 해주었음.
        // 리퀘스트 메서드를 호출하여 현재 요청 http 서블릿 리퀘스트 객체를 얻어옴.

        HttpSession session = request.getSession(); // 현재 요청에 대한 세션을 얻음.
        session.setAttribute("member", kakaoMember); // 세션은 서버 메모리에 저장되고 연결이 끊길경우 소멸되는 데이터로 로그인과 같이 민감한 정보를 다룰때 사용.
        // 이후 세션에서 사용자 정보를 얻어오는 등의 작업이 가능함. 세션의 멤버라는 이름으로 멤버 객체를 저장해 주었음.

        return KakaoDTO.builder() // 빌더를 호출하여 최종적으로 카카오DTO객체를 생성해주었음. 연결한 코드로 객체를 생성할수 있게 빌더가 도와줌.
                .id(id)
                .email(email)
                .nickname(nickname).build();
    }
}
