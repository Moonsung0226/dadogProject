package com.keduit.dadog.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.keduit.dadog.constant.Role;
import com.keduit.dadog.dto.UserDTO;
import lombok.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor  // 기본 생성자 필요
@AllArgsConstructor // 모든 필드를 초기화하는 생성자 필요
@Builder            // 빌더 패턴 추가
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long userNo;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pwd")
    private String userPwd;

    @Column(name = "user_addr")
    private String userAddr;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_tel")
    private String userTel;

    @Column(name = "user_email", unique = true)
    private String userEmail;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private List<Lost> lostList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private List<Protect> protectList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private List<Board> boardList = new ArrayList<>();

    // 정적 메서드로 사용되는 createUser 메서드
    public static User createUser(UserDTO userDTO, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUserId(userDTO.getId());
        user.setUserName(userDTO.getName());
        user.setUserEmail(userDTO.getEmail());
        user.setUserAddr(userDTO.getAddress());
        user.setUserTel(userDTO.getTel());
        String password = passwordEncoder.encode(userDTO.getPassword());
        user.setUserPwd(password);
        user.setRole(Role.USER);
        return user;
    }
}
