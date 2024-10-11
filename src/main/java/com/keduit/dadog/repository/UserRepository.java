package com.keduit.dadog.Repository;

import com.keduit.dadog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId); // 사용자 ID로 사용자 찾기

    @Query("select o from User o where o.userEmail = :userEmail")
    User findByUserEmail(String userEmail); //카카오 로그인 시는 userEmail 로 찾아야함
}
