package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {

}
