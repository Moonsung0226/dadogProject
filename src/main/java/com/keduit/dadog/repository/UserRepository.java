package com.keduit.dadog.repository;

import com.keduit.dadog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserNo(Long userNo); //회원번호로 사용자 찾기

    User findByUserId(String userId); // 사용자 ID로 사용자 찾기

    User findByUserName(String userName);

    @Query("select o from User o where o.userEmail = :userEmail")
    User findByUserEmail(@Param("userEmail") String userEmail); //카카오 로그인 시는 userEmail 로 찾아야함

    List<User> findTop6ByOrderByCreateTimeDesc();

    boolean existsByUserEmail(String userEmail);
    boolean existsByUserId(String userId);
}
