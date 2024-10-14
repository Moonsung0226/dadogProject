package com.keduit.dadog.dto;

import com.keduit.dadog.entity.User;
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
    private String newPassword; // 새 비밀번호
    private String confirmPassword; // 비밀번호 확인
    private String currentPassword; // 현재 비밀번호
    private String tel; // 전화번호
    private String dob; // 생일
    private String gender; // 성별
    private String howDidYouHear; // 어떻게 알았냐
    private String otherSource; // 기타 이유
    private String kakaoId; // 카카오 사용자 ID


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

    // 모든 필드를 포함한 생성자 추가
    public UserDTO(String name, String id, String newPassword, String confirmPassword, String currentPassword, String tel, String dob, String gender, String howDidYouHear, String otherSource, String kakaoId, String email, String password, String address, String nickname) {
        this.name = name;
        this.id = id;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
        this.currentPassword = currentPassword;
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

    public UserDTO createUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getUserName());
        userDTO.setId(user.getUserId());
        userDTO.setEmail(user.getUserEmail());
        userDTO.setTel(user.getUserTel());
        userDTO.setAddress(user.getUserAddr());
        // userDTO.setNickname(user.getUserNickname()); // 필요 시 주석 해제
        return userDTO;
    }

    public User toEntity() {
        User user = new User();
        user.setUserName(name);
        user.setUserId(id);
        user.setUserEmail(email);
        user.setUserTel(tel);
        user.setUserAddr(address);
        return user;
    }
}
