package com.keduit.dadog.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UserDTO {

    private String name; // 사용자 이름
    private String id; // 사용자 ID
    private String confirmPassword; // 비밀번호 확인
    private String tel; // 전화번호
    private String dob; // 생일
    private String gender; // 성별
    private String howDidYouHear; // 어떻게 알았냐
    private String otherSource; // 기타이유
    private long kakaoId; // 카카오 사용자 ID

    @NotEmpty(message = "이메일은 필수 입력입니다")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email; // 이메일

    @NotEmpty(message = "비밀번호는 필수입력입니다")
    @Length(min = 4, max = 16, message = "비밀번호는 4~16 자리로 입력해주세요")
    private String password; // 비밀번호

    @NotEmpty(message = "주소는 필수 입력입니다")
    private String address; // 주소

    private String nickname; // 카카오 사용자 닉네임 추가

    // 기본 생성자 추가
    public UserDTO() {}

    // 모든 필드를 포함한 생성자 추가 (선택사항)
    public UserDTO(String name, String id, String confirmPassword, String tel, String dob, String gender, String howDidYouHear, String otherSource, long kakaoId, String email, String password, String address, String nickname) {
        this.name = name;
        this.id = id;
        this.confirmPassword = confirmPassword;
        this.tel = tel;
        this.dob = dob;
        this.gender = gender;
        this.howDidYouHear = howDidYouHear;
        this.otherSource = otherSource;
        this.kakaoId = kakaoId;
        this.email = email;
        this.password = password;
        this.address = address;
        this.nickname = nickname;
    }
}