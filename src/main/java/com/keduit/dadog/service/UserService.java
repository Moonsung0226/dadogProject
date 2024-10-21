package com.keduit.dadog.service;

import com.keduit.dadog.constant.Occupy;
import com.keduit.dadog.constant.Role;
import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 새로운 사용자를 저장하는 메서드
    public User saveUser(User user) {
        validateUser(user);  // 사용자 중복 검사
        return userRepository.save(user);
    }

//    // 사용자 번호로 사용자를 조회하는 메서드
//    public User getUserByUserNo(Long userNo) {
//        return userRepository.findById(userNo).orElse(null);
//    }

//    // 사용자의 비밀번호를 업데이트하는 메서드
//    public void updatePassword(UserDTO userDTO) {
//        Long userId = Long.parseLong(userDTO.getId());
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
//        user.setUserPwd(passwordEncoder.encode(userDTO.getPassword()));
//        userRepository.save(user);
//    }

    // 사용자 ID 또는 이메일로 사용자를 조회하는 메서드
    public User getUser(String userName) {
        User user = userRepository.findByUserId(userName);
        if (user == null) {
            user = userRepository.findByUserEmail(userName);
        }
        return user;
    }

    // 사용자 중복 검사를 수행하는 private 메서드
    private void validateUser(User user) {
        User findUser = userRepository.findByUserId(user.getUserId());
        if (findUser != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    // 사용자 인증을 확인하는 메서드
    public boolean isValidUser(UserDTO userDTO) {
        User user = userRepository.findByUserId(userDTO.getId());
        return user != null && passwordEncoder.matches(userDTO.getPassword(), user.getUserPwd());
    }

    // 사용자 ID 중복 여부를 확인하는 메서드
    public boolean isIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

    // 이메일 중복 여부를 확인하는 메서드
    public boolean isEmailDuplicate(String userEmail) {
        return userRepository.existsByUserEmail(userEmail);
    }

    // Spring Security의 UserDetailsService 인터페이스 구현 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(email);
        if (user == null) {
            throw new UsernameNotFoundException("아이디 또는 비밀번호를 확인해 주세요.");
        }

        if (user.getOccupy() == Occupy.OFF) {
            throw new UsernameNotFoundException("탈퇴한 사용자입니다.");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getUserPwd())
                .roles(user.getRole().toString())
                .build();
    }

    // 새로운 회원을 등록하는 메서드
    public User registerMember(UserDTO userDTO) {
        User user = User.createUser(userDTO, passwordEncoder);
        validateUser(user);
        return userRepository.save(user);
    }

    // 사용자를 삭제(비활성화)하는 메서드
    public void deleteUserByNo(Long userNo) {
        User user = userRepository.findByUserNo(userNo);
        user.setOccupy(Occupy.OFF);
        userRepository.save(user);
    }

    // 카카오 로그인을 처리하는 메서드
    public User kakaoLogin(UserDTO kakaoDTO) {
        String userId = generateKakaoUserId(kakaoDTO.getKakaoId());
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            user = User.builder()
                    .userEmail(kakaoDTO.getEmail())
                    .userName(kakaoDTO.getNickname())
                    .userId(userId)
                    .userPwd(passwordEncoder.encode("kakao_" + kakaoDTO.getKakaoId()))
                    .role(Role.KAKAO)
                    .build();
            userRepository.save(user);
        } else if (user.getRole() != Role.KAKAO) {
            user.setRole(Role.KAKAO);
            user.setUserName(kakaoDTO.getNickname());
            userRepository.save(user);
        }

        return user;
    }

    // 카카오 사용자 ID를 생성하는 private 메서드
    private String generateKakaoUserId(String kakaoId) {
        return "kakao_" + kakaoId;
    }

//    // 사용자 정보를 업데이트하는 메서드
//    public void updateUser(UserDTO userDTO) {
//        User user = userRepository.findByUserId(userDTO.getId());
//        if (user == null) {
//            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
//        }
//
//        if (user.getRole() == Role.KAKAO) {
//            user.setUserTel(userDTO.getTel());
//            user.setUserAddr(userDTO.getAddress());
//        } else {
//            user.setUserName(userDTO.getName());
//            user.setUserEmail(userDTO.getEmail());
//            user.setUserTel(userDTO.getTel());
//            user.setUserAddr(userDTO.getAddress());
//
//            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
//                user.setUserPwd(passwordEncoder.encode(userDTO.getPassword()));
//            }
//        }
//
//        userRepository.save(user);
//    }

    // 모든 사용자를 조회하는 메서드
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // 최근 가입한 6명의 사용자를 조회하는 메서드
    public List<User> findTop6ByOrderByCreateTimeDesc() {
        return userRepository.findTop6ByOrderByCreateTimeDesc();
    }

    // 사용자의 역할을 업데이트하는 메서드
    public void updateUserRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        user.setRole(role);
        userRepository.save(user);
    }

//    // 사용자 정보를 DTO로 반환하는 메서드
//    public UserDTO getUserInfo(String userId) {
//        User user = userRepository.findByUserId(userId);
//        if (user == null) {
//            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
//        }
//        return new UserDTO().createUserDTO(user);
//    }

    // 사용자의 비밀번호를 변경하는 메서드
    public void changePassword(String userId, String newPassword) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
        user.setUserPwd(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // 사용자 이름으로 사용자를 조회하는 메서드
    public User getUserByUsername(String userName) {
        User user = userRepository.findByUserId(userName);
        if (user == null) {
            user = userRepository.findByUserEmail(userName);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + userName);
        }
        return user;
    }

    // 페이지네이션된 사용자 목록을 반환하는 메서드
    public Page<User> getUserList(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}