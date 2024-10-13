package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    Shelter findByShelNm(String shelNm);
    Page<Shelter> findByShelNmContaining(String shelNm, Pageable pageable);
    Page<Shelter> findByShelCityContaining(String shelCity, Pageable pageable);
    Page<Shelter> findByShelNmContainingOrShelCityContaining(String keyword1, String keyword2, Pageable pageable);
}
