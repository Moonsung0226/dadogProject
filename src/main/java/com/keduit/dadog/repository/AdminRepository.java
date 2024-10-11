package com.keduit.dadog.repository;

import com.keduit.dadog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {
    // 필요에 따라 커스텀 메서드 추가
}
