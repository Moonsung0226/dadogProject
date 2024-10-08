package com.keduit.dadog.entity;


import com.keduit.dadog.constant.Role;
import com.keduit.dadog.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class User extends BaseTimeEntity{

    //회원 삭제 시 해당 유저가 쓴 reply, wish, Application 은 직접 삭제 해주고 유저를 삭제해야함


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
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private List<Lost> lostList = new ArrayList<>(); // 부모가 지워지면 Lost 게시글도 같이 지워질거임

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private List<Protect> protectList = new ArrayList<>(); // 부모가 지워지면 protect 게시글도 같이 지워질거임

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private List<Board> boardList = new ArrayList<>();  //부모가 지워지면 해당 board 가 같이 지워짐

    public static User createUser(UserDTO userDTO, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUserName(userDTO.getName());
        user.setUserEmail(userDTO.getEmail());
        user.setUserAddr(userDTO.getAddress());
        String password = passwordEncoder.encode(userDTO.getPassword());
        user.setUserPwd(password);
        user.setRole(Role.USER);
        return user;
    }
}
