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

@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final AdoptRepository adoptRepository;

    public Long addWish(WishDTO wishDTO, String userName) {
        //TODO: 중복체크 필요
        Adopt adopt = adoptRepository.findById(wishDTO.getAdoptNo()).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUserId(userName);

        if (user == null) {
            user = userRepository.findByUserEmail(userName);
        }
        Wish wishData = wishRepository.findByUserAndAdopt(user, adopt);
        //같은 종을 담으려고 하면
        if (wishData == null) {
            //처음 찜목록 추가시
            Wish wish = new Wish();
            wish = wish.createWish(user, adopt);
            wishRepository.save(wish);
            return wish.getWishNo();
        } else {
            return 0L;
        }
    }
}
