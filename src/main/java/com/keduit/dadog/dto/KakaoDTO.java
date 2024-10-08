package com.keduit.dadog.dto;


import lombok.Builder;
import lombok.Data;
// 카카오 로그인
// 주로 데이터를 전송하거나 서로 다른 계층간의 데이터를 교환하기 위해서 사용. 민감한 정보를 외부로 노출하지않기위해 DTO사용함.
// 회원 정보를 외부에 노출하지 않고 DTO에 담아옴.

@Builder // 롬복에서 제공하는 어노테이션 초기화 로직을 쉽게함. 객체를 생성하는 복잡한 패턴을 초기화. 가독성 높이기위해 사용.
@Data // 투스트링이나 이퀄스 자동생성.
public class KakaoDTO {
// 회원정보를 받아와야 하기때문에 맴버클래와 동일하게 변수 설정함.
    private long id;
    private String email;
    private String nickname;
}
