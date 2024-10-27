package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Sponsor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    // 최신 후원 순으로 정렬
    Page<Sponsor> findAllByOrderBySponNoDesc(Pageable pageable);

    List<Sponsor> findTop6ByOrderBySponNoDesc();
}
