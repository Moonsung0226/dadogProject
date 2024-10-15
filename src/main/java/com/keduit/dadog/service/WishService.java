package com.keduit.dadog.service;

import com.keduit.dadog.dto.WishDTO;
import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.entity.Wish;
import com.keduit.dadog.repository.AdoptRepository;
import com.keduit.dadog.repository.UserRepository;
import com.keduit.dadog.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final AdoptRepository adoptRepository;

    public Long addWish(WishDTO wishDTO, String userName) {
        Adopt adopt = adoptRepository.findById(wishDTO.getAdoptNo()).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUserId(userName);

        if (user == null) {
            user = userRepository.findByUserEmail(userName);
        }
        Wish wishData = wishRepository.findByUserAndAdopt(user, adopt);
        if (wishData == null) {
            Wish wish = new Wish();
            wish = wish.createWish(user, adopt);
            wishRepository.save(wish);
            return wish.getWishNo();
        } else {
            return 0L;
        }
    }

    public List<Wish> getWishListByUser(User user) {
        if (user == null) {
            return List.of();
        }
        return wishRepository.findByUser(user);
    }
}