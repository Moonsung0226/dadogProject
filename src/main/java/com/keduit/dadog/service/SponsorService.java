package com.keduit.dadog.service;

import com.keduit.dadog.entity.Sponsor;
import com.keduit.dadog.repository.SponsorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SponsorService {
    private final SponsorRepository sponsorRepository;

    public Page<Sponsor> getSponsorList(Pageable pageable) {
        return sponsorRepository.findAllByOrderBySponNoDesc(pageable);
    }

    public List<Sponsor> findTop6ByOrderBySponNoDesc() {
        return sponsorRepository.findTop6ByOrderBySponNoDesc();
    }
}
