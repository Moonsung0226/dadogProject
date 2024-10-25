package com.keduit.dadog.repository;

import com.keduit.dadog.dto.SearchDTO;
import com.keduit.dadog.entity.Lost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LostRepositoryCustom {

    Page<Lost> getAdoptListPage(SearchDTO searchDTO, Pageable pageable);

    Page<Lost> getMyLostPage(Pageable pageable, Long userNo);
}
