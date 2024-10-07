package com.keduit.dadog.service;

import com.keduit.dadog.Repository.AdoptRepository;
import com.keduit.dadog.dto.AdoptDTO;
import com.keduit.dadog.entity.Adopt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class AdoptService {

    private final AdoptRepository adoptRepository;

    @Transactional(readOnly = true)
    public List<Adopt> getAdoptList(){
        return adoptRepository.findAll();
    }

}
