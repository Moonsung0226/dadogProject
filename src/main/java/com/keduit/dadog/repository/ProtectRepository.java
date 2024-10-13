package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Protect;
import com.keduit.dadog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProtectRepository extends JpaRepository<Protect, Long>, ProtectRepositoryCustom {

    List<Protect> findByUser(User user);

    Protect findByProNo(Long proNo);
}
