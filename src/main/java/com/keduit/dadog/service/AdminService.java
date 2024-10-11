package com.keduit.dadog.service;

import com.keduit.dadog.entity.Lost;
import com.keduit.dadog.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private LostService lostService;

    // 실종 신고 리스트 가져오기
    public Page<Lost> getLostList(Pageable pageable) {
        // 필요한 경우 SearchDTO를 사용하여 검색 조건을 지정
        return lostService.getLostList(null, pageable);
    }

    // 특정 실종 신고 삭제하기
    public void deleteLost(Long lostNo) {
        lostService.deleteLost(lostNo);
    }
}
