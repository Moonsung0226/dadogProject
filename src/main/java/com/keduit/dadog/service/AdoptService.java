package com.keduit.dadog.service;

import com.keduit.dadog.dto.AdoptDTO;
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
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class AdoptService {

    private final AdoptRepository adoptRepository;
    private final ApplicationRepository applicationRepository;

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


    // 입양현황
    public AdoptDTO getApplicationByAppNo(Long appNo) {
        // Application 엔티티를 appNo로 조회
        Application application = applicationRepository.findById(appNo)
                .orElseThrow(() -> new EntityNotFoundException("Application not found with appNo: " + appNo));

        // Application에서 Adopt 정보 가져오기
        Adopt adopt = application.getAdopt();

        // Adopt 정보를 DTO로 변환하여 반환
        return new AdoptDTO().mainAdopt(adopt);
    }

    @Transactional(readOnly = true)
    public List<AdoptDTO> getApplicationsByUserNo(Long userNo) {
        List<Application> applications = applicationRepository.findByUser_UserNo(userNo);
        return applications.stream()
                .map(application -> {
                    Adopt adopt = application.getAdopt();
                    AdoptDTO adoptDTO = new AdoptDTO().mainAdopt(adopt);
                    adoptDTO.setAdopt_no(application.getAppNo());

                    return adoptDTO;
                })
                .collect(Collectors.toList());
    }
}
