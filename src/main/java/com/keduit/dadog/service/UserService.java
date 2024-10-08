package com.keduit.dadog.service;

import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public com.keduit.dadog.entity.User saveUser(com.keduit.dadog.entity.User user) {
        validateUser(user);
        return userRepository.save(user);
    }

    private void validateUser(com.keduit.dadog.entity.User user) {
        com.keduit.dadog.entity.User findUser = userRepository.findByUserId(user.getUserId());
        if (findUser != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.keduit.dadog.entity.User user = userRepository.findByUserId(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(user.getUserId())
                .password(user.getUserPwd())
                .roles(user.getRole().toString())
                .build();
    }

    public com.keduit.dadog.entity.User registerMember(UserDTO userDTO) {
        com.keduit.dadog.entity.User user = com.keduit.dadog.entity.User.createUser(userDTO, passwordEncoder);
        validateUser(user); // 중복 검증
        return userRepository.save(user);
    }

}
