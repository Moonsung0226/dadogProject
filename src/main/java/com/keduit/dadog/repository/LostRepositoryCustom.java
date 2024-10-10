package com.keduit.dadog.repository;

import com.keduit.dadog.dto.LostSearchDTO;
import com.keduit.dadog.entity.Lost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LostRepositoryCustom {

    Page<Lost> getAdoptListPage(LostSearchDTO lostSearchDTO, Pageable pageable);
}
