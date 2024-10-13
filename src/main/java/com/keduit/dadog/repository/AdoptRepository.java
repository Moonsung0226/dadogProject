package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Adopt;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptRepository extends JpaRepository<Adopt, Long> , AdoptRepositoryCustom {
}