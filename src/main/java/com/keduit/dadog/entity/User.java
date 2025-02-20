package com.keduit.dadog.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.keduit.dadog.constant.Occupy;
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
@ToString(exclude = {"lostList", "protectList", "boardList"})  // StackOverflowError 발생. 무한루프때문에 양방향 관계 필드를 제외
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

//    @Column(name = "user_nickname")
//    private String userNickname;

    @Column(name = "user_email", unique = true)
    private String userEmail;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "occupy")
    @Enumerated(EnumType.STRING)
    private Occupy occupy;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private List<Lost> lostList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private List<Protect> protectList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    @JsonManagedReference
    private List<Board> boardList = new ArrayList<>();


    // 회원으로 가입을 할껀지 관리자로 가입을 할껀지.
    // 정적 메서드로 사용되는 createUser 메서드
    public static User createUser(UserDTO userDTO, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUserId(userDTO.getId());
        user.setUserName(userDTO.getName());
//        user.setUserNickname(userDTO.getNickname());
        user.setUserEmail(userDTO.getEmail());
        user.setUserAddr(userDTO.getAddress());
        user.setUserTel(userDTO.getTel());
        String password = passwordEncoder.encode(userDTO.getPassword());
        user.setUserPwd(password);
        user.setRole(Role.USER);
        user.setOccupy(Occupy.ON);
        return user;
    }

    // 카카오 사용자를 위한 정적 메서드
    public static User createKakaoUser(String id, String name, String email) {
        User user = new User();
        user.setUserId(id);
        user.setUserName(name);
        user.setUserEmail(email);
        // 카카오 사용자는 비밀번호가 없으므로 임의의 값을 설정하거나 null로.
        user.setUserPwd(null);
        user.setRole(Role.KAKAO);
        user.setOccupy(Occupy.ON);
        return user;
    }
}
