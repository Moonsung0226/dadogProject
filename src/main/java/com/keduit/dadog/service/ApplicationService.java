package com.keduit.dadog.service;

import com.keduit.dadog.constant.AdoptWait;
import com.keduit.dadog.constant.Current;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final AdoptRepository adoptRepository;

    public long countPendingApplications() {
        return applicationRepository.countByAdoptWaitStatus(AdoptWait.PENDING);
    }

    public Page<ApplicationDTO> getApplicationList(Pageable pageable) {
        return applicationRepository.getApplicationListPage(pageable)
                .map(this::convertToDTO);
    }

    public Page<ApplicationDTO> getApplicationListByStatus(AdoptWait status, Pageable pageable) {
        return applicationRepository.findByAdoptWaitStatus(status, pageable)
                .map(this::convertToDTO);
    }

    public void updateAdoptWaitStatus(Long appNo, String status) {
        Application application = applicationRepository.findById(appNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));

        // 상태 업데이트
        application.setStatus(status); // Application의 상태 업데이트

        // Adopt의 current 상태 업데이트
        if ("REJECTED".equalsIgnoreCase(status)) {
            application.getAdopt().setCurrent(Current.Y); // Adopt의 current 상태를 Y로 설정
        } else {
            application.getAdopt().setCurrent(Current.N);
        }

        applicationRepository.save(application); // 변경사항 저장
    }

    public void updateAdoptCurrentStatus() {
        List<Adopt> adoptList = adoptRepository.findAll(); // 모든 Adopt 엔티티 가져오기
        LocalDate today = LocalDate.now();

        for (Adopt adopt : adoptList) {
            LocalDate adoptEndDate = LocalDate.parse(adopt.getAdoptEdt(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // 공고 종료일이 오늘보다 이전인 경우 current를 'N'으로 변경
            if (adoptEndDate.isBefore(today)) {
                adopt.setCurrent(Current.N);
            }
        }

        // 변경된 Adopt 엔티티 저장
        adoptRepository.saveAll(adoptList);
    }

    public ApplicationDTO findApplicationDTOByAppNo(Long appNo) {
        Application application = applicationRepository.findByAppNo(appNo)
                .orElseThrow(() -> new EntityNotFoundException("Application not found with appNo: " + appNo));
        return convertToDTO(application);
    }

    private ApplicationDTO convertToDTO(Application application) {
        // application에서 필요한 정보를 DTO로 변환
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

    @Transactional
    public void updateAdoptWaitStatuss(Long appNo, String status) {
        Application application = applicationRepository.findByAppNo(appNo)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));
        application.setAdoptWaitStatus(AdoptWait.valueOf(status.toUpperCase()));
        applicationRepository.save(application);
    }

    // current Y/N 메서드
    @Transactional
    public Long applyForAdoption(Long adoptNo, String userName) {
        User user = userRepository.findByUserId(userName);
        if (user == null) {
            user = userRepository.findByUserEmail(userName);
        }

        Adopt adopt = adoptRepository.findById(adoptNo)
                .orElseThrow(() -> new EntityNotFoundException("Adopt not found with id: " + adoptNo));

        if (adopt.getCurrent() != Current.Y) {
            throw new IllegalStateException("This dog is not available for adoption.");
        }

        Application application = new Application();
        application.setUser(user);
        application.setAdopt(adopt);
        application.setAdoptWaitStatus(AdoptWait.PENDING);

        // 유기견의 current 상태를 N으로 변경
        adopt.setCurrent(Current.N);  // 신청 후 상태를 'N'으로 변경
        adoptRepository.save(adopt);   // 변경 사항 저장

        applicationRepository.save(application);

        return application.getAppNo();
    }

}