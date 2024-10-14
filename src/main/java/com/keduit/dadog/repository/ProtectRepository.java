package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Protect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProtectRepository extends JpaRepository<Protect, Long>, ProtectRepositoryCustom {

    Protect findByProNo(Long proNo);

    List<Protect> findTop6ByOrderByCreateTimeDesc();
}
