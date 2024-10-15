package com.keduit.dadog.repository;

import com.keduit.dadog.constant.AdoptWait;
import com.keduit.dadog.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    long countByAdoptWaitStatus(AdoptWait adoptWaitStatus);
}
