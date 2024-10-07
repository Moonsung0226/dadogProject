package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    Shelter findByShelNm(String shelNm);
    List<Shelter> findByShelNmContaining(String shelNm); // 보호소명으로 검색
    List<Shelter> findByShelCityContaining(String shelCity); // 지역으로 검색
    List<Shelter> findByShelNmAndShelCityContaining(String keyword); // 보호소명과 지역으로 검색
}
