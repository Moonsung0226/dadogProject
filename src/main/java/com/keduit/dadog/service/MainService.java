package com.keduit.dadog.service;

import com.keduit.dadog.dto.AdoptDTO;
import com.keduit.dadog.dto.LostDTO;
import com.keduit.dadog.dto.ProtectDTO;
import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.entity.Lost;
import com.keduit.dadog.entity.Protect;
import com.keduit.dadog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final AdoptRepositoryCustomImpl adoptRepositoryCustom;
    private final LostRepositoryCustomImpl lostRepositoryCustom;
    private final ProtectRepositoryCustomImpl protectRepositoryCustom;


    public List<AdoptDTO> getMainAdopts() {
        List<Adopt> adopts = adoptRepositoryCustom.mainAdoptList();
        List<AdoptDTO> adoptDTOList = new ArrayList<>();
        for(Adopt adopt : adopts) {
            AdoptDTO adoptDTO = new AdoptDTO();
            adoptDTO = adoptDTO.mainAdopt(adopt);
            adoptDTOList.add(adoptDTO);
        }
        return adoptDTOList;
    }

    public List<LostDTO> getMainLosts() {
        List<Lost> losts = lostRepositoryCustom.mainLostList();
        List<LostDTO> lostDTOList = new ArrayList<>();
        for(Lost lost : losts) {
            LostDTO lostDTO = new LostDTO();
            lostDTO = lostDTO.mainLost(lost);
            lostDTOList.add(lostDTO);
        }
        return lostDTOList;
    }

    public List<ProtectDTO> getMainProtects() {
        List<Protect> protects = protectRepositoryCustom.mainProtectList();
        List<ProtectDTO> protectDTOList = new ArrayList<>();
        for(Protect protect : protects) {
            ProtectDTO protectDTO = new ProtectDTO();
            protectDTO = protectDTO.mainProtect(protect);
            protectDTOList.add(protectDTO);
        }
        return protectDTOList;
    }
}
