package com.keduit.dadog.service;

import com.keduit.dadog.Repository.AdoptRepository;
import com.keduit.dadog.dto.AdoptDTO;
import com.keduit.dadog.dto.AdoptSearchDTO;
import com.keduit.dadog.entity.Adopt;
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

    public Adopt getAdopt(Long adopt_no) {
        return adoptRepository.findById(adopt_no).orElseThrow(EntityNotFoundException::new);
    }

}
