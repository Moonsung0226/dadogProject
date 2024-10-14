package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Adopt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptRepository extends JpaRepository<Adopt, Long> , AdoptRepositoryCustom {
    List<Adopt> findTop6ByOrderByAdoptEdtDesc();
}