package com.keduit.dadog.service;

import com.keduit.dadog.dto.WishDTO;
import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.entity.Lost;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WishService {

    // 필요한 리포지토리들을 주입받음
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final AdoptRepository adoptRepository;

    // 위시리스트에 항목을 추가하는 메서드
    public Long addWish(WishDTO wishDTO, String userName) {
        // 입양 엔티티를 찾음. 없으면 예외 발생
        Adopt adopt = adoptRepository.findById(wishDTO.getAdoptNo()).orElseThrow(EntityNotFoundException::new);

        // 사용자를 ID로 찾고, 없으면 이메일로 찾음
        User user = userRepository.findByUserId(userName);
        if (user == null) {
            user = userRepository.findByUserEmail(userName);
        }

        // 해당 사용자와 입양 항목에 대한 위시 항목이 이미 존재하는지 확인
        Wish wishData = wishRepository.findByUserAndAdopt(user, adopt);
        if (wishData == null) {
            // 위시 항목이 없으면 새로 생성하고 저장
            Wish wish = new Wish();
            wish = wish.createWish(user, adopt);
            wishRepository.save(wish);
            return wish.getWishNo();
        } else {
            // 이미 존재하면 0을 반환
            return 0L;
        }
    }

    // 특정 사용자의 위시리스트를 가져오는 메서드
    public List<Wish> getWishListByUser(User user) {
        if (user == null) {
            return List.of(); // 사용자가 null이면 빈 리스트 반환
        }
        return wishRepository.findByUser(user);
    }

    // 위시리스트 항목이 해당 사용자의 것인지 확인하는 메서드
    public boolean wishValidation(String userName, Long wishNo) {
        // 사용자를 ID로 찾고, 없으면 이메일로 찾음
        User user = userRepository.findByUserId(userName);
        if (user == null) {
            user = userRepository.findByUserEmail(userName);
        }

        // 위시 항목을 찾음. 없으면 예외 발생
        Wish wish = wishRepository.findById(wishNo)
                .orElseThrow(() -> new EntityNotFoundException("Wish not found with wishNo : " + wishNo));

        // 위시 항목의 사용자 ID와 현재 사용자의 ID가 일치하는지 확인
        return Objects.equals(wish.getUser().getUserNo(), user.getUserNo());
    }

    // 위시리스트 항목을 삭제하는 메서드
    public void deleteWish(Long wishNo) {
        // 위시 항목을 찾음. 없으면 예외 발생
        Wish wish = wishRepository.findById(wishNo)
                .orElseThrow(() -> new EntityNotFoundException("Wish not found with wishNo : " + wishNo));

        // 찾은 위시 항목을 삭제
        wishRepository.delete(wish);
    }
}