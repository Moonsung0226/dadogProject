package com.keduit.dadog.repository;

import com.keduit.dadog.constant.AdoptWait;
import com.keduit.dadog.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;  // 이 줄을 추가하세요
import java.util.Optional;
import java.util.stream.DoubleStream;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    long countByAdoptWaitStatus(AdoptWait adoptWaitStatus);

    @Query("SELECT a FROM Application a")
    Page<Application> getApplicationListPage(Pageable pageable);

    Optional<Application> findByAppNo(Long appNo);

    // 상태에 따른 신청 목록 조회
    Page<Application> findByAdoptWaitStatus(AdoptWait adoptWaitStatus, Pageable pageable);

    List<Application> findByUser_UserNo(Long userNo); // User 엔티티의 userNo 속성을 참조

    @Query("SELECT a FROM Application a WHERE a.user.userNo = :userNo")
    Page<Application> getApplicationUserId(Long userNo, Pageable pageable);

    // User 엔티티의 userNo 속성을 참조하여 입양 신청 목록을 가져오는 메서드
    Page<Application> findByUser_UserNo(Long userNo, Pageable pageable);
}

