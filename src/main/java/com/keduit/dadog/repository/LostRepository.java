package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Lost;
import com.keduit.dadog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LostRepository extends JpaRepository<Lost, Long>, LostRepositoryCustom {

    @Query("select o from Lost o where o.user = :user")
    Lost findByUserNo(User user);

    List<Lost> findByUser(User user);

    Lost findByLostNo(Long lostNo);

    List<Lost> findTop6ByOrderByLostDateDesc();
}
