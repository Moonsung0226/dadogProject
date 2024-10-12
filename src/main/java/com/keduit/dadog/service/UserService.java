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

    public User saveUser(User user) {
        validateUser(user);
        return userRepository.save(user);
    }

    public User getUser(String userName) {
        // 카카오 유저를 위해 userEmail로 한번 더 검색
        User user = userRepository.findByUserId(userName);
        if (user == null) {
            user = userRepository.findByUserEmail(userName);
        }
        return user;
    }

    private void validateUser(User user) {
        User findUser = userRepository.findByUserId(user.getUserId());
        if (findUser != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    public boolean isValidUser(UserDTO userDTO) {
        User user = userRepository.findByUserId(userDTO.getId());
        if (user != null && passwordEncoder.matches(userDTO.getPassword(), user.getUserPwd())) {
            return true;
        }
        return false;
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

    public User registerMember(UserDTO userDTO) {
        User user = User.createUser(userDTO, passwordEncoder);
        validateUser(user);
        return userRepository.save(user);
    }

    // 카카오 로그인 처리
    public User kakaoLogin(UserDTO kakaoDTO) {
        String userId = kakaoDTO.getEmail().split("@")[0];
        User user = userRepository.findByUserId(userId);
        System.out.println("----------------user Service 유저체크");
        if (user == null) {
            user = User.builder()
                    .userEmail(kakaoDTO.getEmail())
                    .userName(kakaoDTO.getName()) // 닉네임 대신 이름 사용
                    .userId(userId)
                    .userPwd(passwordEncoder.encode("kakao_password"))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }

        return user;
    }

    public void updateUser(UserDTO userDTO) {
        // 사용자 ID 유효성 검사
        if (userDTO.getId() == null || userDTO.getId().isEmpty()) {
            throw new IllegalArgumentException("사용자 ID가 비어있습니다.");
        }

        // 쉼표가 포함된 경우 처리
        if (userDTO.getId().contains(",")) {
            throw new IllegalArgumentException("사용자 ID에 쉼표를 포함할 수 없습니다.");
        }

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
}
