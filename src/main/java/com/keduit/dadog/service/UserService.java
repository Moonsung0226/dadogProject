package com.keduit.dadog.service;

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

    // 사용자 저장
    public User saveUser(User user) {
        validateUser(user);  // 사용자 유효성 검사
        return userRepository.save(user);
    }

    public User getUserByUserNo(Long userNo) {
        return userRepository.findById(userNo).orElse(null); // ID로 사용자 조회
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
    // 카카오 로그인 처리 (수정)
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

    // 카카오 사용자 ID 생성 (새로운 메서드)
    private String generateKakaoUserId(String kakaoId) {
        return "kakao_" + kakaoId;
    }

    // 사용자 정보 업데이트 (수정)
    public void updateUser(UserDTO userDTO) {
        User user = userRepository.findByUserId(userDTO.getId());
        if (user == null) {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        if (user.getRole() == Role.KAKAO) {
            user.setUserTel(userDTO.getTel());
            user.setUserAddr(userDTO.getAddress());
        } else {
            user.setUserName(userDTO.getName());
            user.setUserEmail(userDTO.getEmail());
            user.setUserTel(userDTO.getTel());
            user.setUserAddr(userDTO.getAddress());

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setUserPwd(passwordEncoder.encode(userDTO.getPassword()));
            }
        }

        userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll(); // 모든 User 엔티티를 반환
    }

    public List<User> findTop6ByOrderByCreateTimeDesc() {
        return userRepository.findTop6ByOrderByCreateTimeDesc();
    }

    public void updateUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        user.setRole(newRole);
        userRepository.save(user);
    }
    // 변경된 비밀번호로 로그인하기위해(1) ->  이 메서드에서 사용자의 비밀번호를 변경.
    public UserDTO getUserInfo(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
        return new UserDTO().createUserDTO(user); // UserDTO로 변환하여 반환
    }

    // 변경된 비밀번호로 로그인하기위해(2) -> getUserInfo 메서드로 사용자 정보를 조회하는 메서드를 구현. 비밀번호 변경 전 사용자의 정보를 확인.
    public void changePassword(String userId, String newPassword) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
        user.setUserPwd(passwordEncoder.encode(newPassword)); // 새 비밀번호로 업데이트
        userRepository.save(user); // 변경 사항 저장
    }

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
    public Page<User> getUserList(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
