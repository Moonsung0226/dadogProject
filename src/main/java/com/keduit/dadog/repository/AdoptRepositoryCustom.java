package com.keduit.dadog.repository;

import com.keduit.dadog.dto.AdoptSearchDTO;
import com.keduit.dadog.entity.Adopt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdoptRepositoryCustom {

    Page<Adopt> getAdoptListPage(AdoptSearchDTO adoptSearchDTO, Pageable pageable);
}
