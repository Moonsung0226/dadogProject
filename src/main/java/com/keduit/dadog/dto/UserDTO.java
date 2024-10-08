package com.keduit.dadog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class UserDTO {

    private String name;

    @NotEmpty(message = "이메일은 필수 입력입니다")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;

    @NotEmpty(message = "비밀번호는 필수입력입니다")
    @Length(min = 4, max = 16, message = "비밀번호는 4~16 자리로 입력해주세요")
    private String password;

    @NotEmpty(message = "주소는 필수 입력입니다")
    private String address;
}
