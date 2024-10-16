package com.keduit.dadog.service;

import com.keduit.dadog.constant.AdoptWait;
import com.keduit.dadog.dto.ApplicationDTO;
import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.entity.Application;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.AdoptRepository;
import com.keduit.dadog.repository.ApplicationRepository;
import com.keduit.dadog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final AdoptRepository adoptRepository;

    // PENDING 상태의 신청 수를 계산
    public long countPendingApplications() {
        return applicationRepository.countByAdoptWaitStatus(AdoptWait.PENDING);
    }

    // 모든 신청 목록을 페이지네이션하여 가져옴
    public Page<ApplicationDTO> getApplicationList(Pageable pageable) {
        return applicationRepository.getApplicationListPage(pageable)
                .map(this::convertToDTO);
    }

    // 특정 상태의 신청 목록을 페이지네이션하여 가져옴
    public Page<ApplicationDTO> getApplicationListByStatus(AdoptWait status, Pageable pageable) {
        return applicationRepository.findByAdoptWaitStatus(status, pageable)
                .map(this::convertToDTO);
    }

    // 특정 신청 번호로 신청 정보를 가져옴
    public ApplicationDTO findApplicationDTOByAppNo(Long appNo) {
        Application application = applicationRepository.findByAppNo(appNo)
                .orElseThrow(() -> new EntityNotFoundException("Application not found with appNo : " + appNo));
        return convertToDTO(application);
    }

    // Application 엔티티를 DTO로 변환
    private ApplicationDTO convertToDTO(Application application) {
        return new ApplicationDTO(
                application.getAppNo(),
                application.getUser().getUserNo(),
                application.getUser().getUserId(),
                application.getUser().getUserName(),
                application.getUser().getUserEmail(),
                application.getUser().getUserTel(),
                application.getAdopt().getAdoptNo(),
                application.getAdopt().getAdoptKind(),
                application.getAdopt().getAdoptAge(),
                application.getAdopt().getAdoptEdt(),
                application.getAdopt().getAdoptCareNm(),
                application.getAdopt().getAdoptCareAddr(),
                application.getAdopt().getAdoptCareTel(),
                application.getAdoptWaitStatus()
        );
    }

    // 신청 상태를 업데이트
    public void updateAdoptWaitStatus(Long appNo, String status) {
        Application application = applicationRepository.findByAppNo(appNo)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));
        application.setAdoptWaitStatus(AdoptWait.valueOf(status.toUpperCase()));
        applicationRepository.save(application);
    }

    // 새로운 입양 신청을 생성 메서드 추가.
    @Transactional
    public Long applyForAdoption(Long adoptNo, String userName) {
        // 사용자 찾기
        User user = userRepository.findByUserId(userName);
        if (user == null) {
            user = userRepository.findByUserEmail(userName);
        }

        // 입양 대상 동물 찾기
        Adopt adopt = adoptRepository.findById(adoptNo)
                .orElseThrow(() -> new EntityNotFoundException("Adopt not found with id: " + adoptNo));

        // 새로운 Application 엔티티 생성 및 저장
        Application application = new Application();
        application.setUser(user);
        application.setAdopt(adopt);
        application.setAdoptWaitStatus(AdoptWait.PENDING);

        applicationRepository.save(application);

        return application.getAppNo();
    }
}