package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Lost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostRepository extends JpaRepository<Lost, Long>,LostRepositoryCustom {

}
