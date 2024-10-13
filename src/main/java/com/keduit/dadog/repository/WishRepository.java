package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByUser(User user);
    Wish findByUserAndAdopt(User user, Adopt adopt);
}
