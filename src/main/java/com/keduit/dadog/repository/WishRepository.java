package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Wish findByUser(User user);
    Wish findByUserAndAdopt(User user, Adopt adopt);
}
