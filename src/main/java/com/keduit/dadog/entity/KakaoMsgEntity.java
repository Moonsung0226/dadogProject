package com.keduit.dadog.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

// 카카오 로그인
// 주로 API 응답 등에 사용. 클라이언트에게 결과 데이터를 전달할때 활용.


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KakaoMsgEntity {

    private String msg;     // 메시지 내용
    private Object result;  // 결과 데이터

    // 매개변수가 있는 생성자
    public KakaoMsgEntity(String msg, Object result) { // String msg => Success 사용. , Object result => kakaoInfo를 사용.
        this.msg = msg;
        this.result = result;
    }
}
