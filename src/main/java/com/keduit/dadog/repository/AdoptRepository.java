package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Adopt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdoptRepository extends JpaRepository<Adopt, Long> , AdoptRepositoryCustom {

    @Query("select o from Adopt o where o.adoptCareNm = :careNm and o.adoptImgUrl = :popfile")
    Optional<Adopt> findByCareNmAndImgUrl(String careNm, String popfile);
    List<Adopt> findTop6ByOrderByAdoptEdtDesc();
}