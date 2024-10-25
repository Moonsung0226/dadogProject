package com.keduit.dadog.repository;

import com.keduit.dadog.dto.SearchDTO;
import com.keduit.dadog.entity.Protect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProtectRepositoryCustom {

    Page<Protect> getProtectListPage(SearchDTO searchDTO, Pageable pageable);

    Page<Protect> getMyProtectPage(Pageable pageable, Long userNo);

}
