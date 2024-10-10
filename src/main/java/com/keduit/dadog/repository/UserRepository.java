package com.keduit.dadog.repository;

import com.keduit.dadog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId); // 사용자 ID로 사용자 찾기
}
