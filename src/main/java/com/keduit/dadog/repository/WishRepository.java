package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Wish 엔티티에 대한 데이터 액세스를 위한 리포지토리 인터페이스
public interface WishRepository extends JpaRepository<Wish, Long> {

    // 특정 사용자의 모든 위시리스트 항목을 조회하는 메서드
    List<Wish> findByUser(User user);

    // 특정 사용자와 입양 게시글에 해당하는 위시리스트 항목을 조회하는 메서드
    Wish findByUserAndAdopt(User user, Adopt adopt);
}
