package com.keduit.dadog.service;

import com.keduit.dadog.dto.AdoptSearchDTO;
import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.entity.Application;
import com.keduit.dadog.repository.AdoptRepository;
import com.keduit.dadog.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class AdoptService {

    private final AdoptRepository adoptRepository;

    @Transactional(readOnly = true)
    public Page<Adopt> getAdoptList(AdoptSearchDTO adoptSearchDTO, Pageable pageable) {
        return adoptRepository.getAdoptListPage(adoptSearchDTO, pageable);
    }

    public Adopt getAdopt(Long adoptNo) {
        return adoptRepository.findById(adoptNo).orElseThrow(EntityNotFoundException::new);
    }

    // adopt_edt를 기준으로 최근 6개의 Adopt 데이터를 가져오는 메서드
    public List<Adopt> findTop6ByOrderByAdoptEdtDesc() {
        return adoptRepository.findTop6ByOrderByAdoptEdtDesc();
    }

    public Adopt findByAdoptNo(Long adoptNo) {
        return adoptRepository.findById(adoptNo).orElseThrow(() -> new EntityNotFoundException("Adopt not found with adoptNo : " + adoptNo));
    }

    public void deleteAdopt(Long adoptNo) {
        adoptRepository.deleteById(adoptNo);
    }
}