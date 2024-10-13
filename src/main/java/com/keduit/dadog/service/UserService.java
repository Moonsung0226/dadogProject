package com.keduit.dadog.service;

import com.keduit.dadog.constant.Role;
import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 저장
    public User saveUser(User user) {
        validateUser(user);  // 사용자 유효성 검사
        return userRepository.save(user);
    }



    // 비밀번호 업데이트
    public void updatePassword(UserDTO userDTO) {
        Long userId = Long.parseLong(userDTO.getId());  // String을 Long으로 변환
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        user.setUserPwd(passwordEncoder.encode(userDTO.getPassword()));  // 암호화된 비밀번호 저장
        userRepository.save(user);
    }


    // 사용자 정보 가져오기 (유저 ID 또는 이메일 기반)
    public User getUser(String userName) {
        User user = userRepository.findByUserId(userName);
        if (user == null) {
            user = userRepository.findByUserEmail(userName);
        }
        return user;
    }

    // 사용자 유효성 검사
    private void validateUser(User user) {
        User findUser = userRepository.findByUserId(user.getUserId());
        if (findUser != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    // 비밀번호 유효성 검사
    public boolean isValidUser(UserDTO userDTO) {
        User user = userRepository.findByUserId(userDTO.getId());
        return user != null && passwordEncoder.matches(userDTO.getPassword(), user.getUserPwd());
    }

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

    // 회원 등록
    public User registerMember(UserDTO userDTO) {
        User user = User.createUser(userDTO, passwordEncoder);
        validateUser(user);
        return userRepository.save(user);
    }

    // 카카오 로그인 처리 (닉네임을 이름으로 사용)
    public User kakaoLogin(UserDTO kakaoDTO) {
        String userId = kakaoDTO.getEmail().split("@")[0];
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            user = User.builder()
                    .userEmail(kakaoDTO.getEmail())
                    .userName(kakaoDTO.getNickname()) // 닉네임을 이름으로 사용
                    .userId(userId)
                    .userPwd(passwordEncoder.encode("kakao_password"))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        } else {
            // 기존 사용자의 경우 닉네임 업데이트
            user.setUserName(kakaoDTO.getNickname());
            userRepository.save(user);
        }

        return user;
    }

    // 사용자 정보 업데이트
    public void updateUser(UserDTO userDTO) {
        // 사용자 존재 여부 확인
        User user = userRepository.findByUserId(userDTO.getId());
        if (user == null) {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        // 사용자 정보 업데이트
        user.setUserName(userDTO.getName());
        user.setUserEmail(userDTO.getEmail());
        user.setUserTel(userDTO.getTel());
        user.setUserAddr(userDTO.getAddress());

        // 비밀번호가 제공된 경우에만 업데이트
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setUserPwd(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(user);
    }

    @Transactional
    public void changePassword(String username, String newPassword) {
        User user = userRepository.findByUserId(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        user.setUserPwd(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
