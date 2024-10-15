package com.keduit.dadog.repository;

import com.keduit.dadog.constant.AdoptWait;
import com.keduit.dadog.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    long countByAdoptWaitStatus(AdoptWait adoptWaitStatus);

    @Query("SELECT a FROM Application a")
    Page<Application> getApplicationListPage(Pageable pageable);

    Optional<Application> findByAppNo(Long appNo);

    // 상태에 따른 신청 목록 조회
    Page<Application> findByAdoptWaitStatus(AdoptWait adoptWaitStatus, Pageable pageable);
}