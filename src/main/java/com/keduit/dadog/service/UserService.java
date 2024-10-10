package com.keduit.dadog.service;

import com.keduit.dadog.constant.Role;
import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.UserRepository; // UserRepository 임포트 추가
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository; // UserRepository 주입
    private final PasswordEncoder passwordEncoder;

    // 새로운 사용자 저장
    public User saveUser(User user) {
        validateUser(user);
        return userRepository.save(user);
    }

    // 중복된 사용자 검증
    private void validateUser(User user) {
        User findUser = userRepository.findByUserId(user.getUserId());
        if (findUser != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    // UserDetailsService 인터페이스 구현 (Spring Security에서 사용)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getUserPwd())
                .roles(user.getRole().toString())
                .build();
    }

    // 회원 등록 메소드
    public User registerMember(UserDTO userDTO) {
        User user = User.createUser(userDTO, passwordEncoder);
        validateUser(user); // 중복 검증
        return userRepository.save(user);
    }

    public User kakaoLogin(UserDTO kakaoDTO) {
        String userId = kakaoDTO.getEmail().split("@")[0]; // @ 앞 부분만 아이디로 사용
        User user = userRepository.findByUserId(userId);
        System.out.println("----------------user Service 유저체크");
        if (user == null) {
            user = User.builder()
                    .userEmail(kakaoDTO.getEmail())
                    .userName(kakaoDTO.getNickname())
                    .userId(userId)  // @ 앞 부분을 아이디로 사용
                    .userPwd(passwordEncoder.encode("kakao_password")) // 임시 비밀번호
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }

        return user; // 로그인 처리된 사용자 반환
    }

}