package com.keduit.dadog.service;

import com.keduit.dadog.dto.LostDTO;
import com.keduit.dadog.dto.LostSearchDTO;
import com.keduit.dadog.entity.Lost;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.LostRepository;
import com.keduit.dadog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LostService {

    private final LostRepository lostRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Value("${lostImgLocation}")
    private String lostImgLocation;

    //실종신고 게시판
    @Transactional(readOnly = true)
    public Page<Lost> getLostList(LostSearchDTO lostSearchDTO, Pageable pageable) {
        return lostRepository.getAdoptListPage(lostSearchDTO, pageable);
    }

    public Lost getLost(Long lostNo){
        return lostRepository.findById(lostNo).orElseThrow(EntityNotFoundException::new);
    }

    //실종신고 등록
    public Long addLost(LostDTO lostDTO, String userName, MultipartFile lostImg) throws Exception{
        User user = userRepository.findByUserId(userName);
        if(user == null) {
            System.out.println("--------------> 여기");
            user = userRepository.findByUserEmail(userName);
        }

        Lost lost = Lost.createLost(lostDTO, userName, user);
        String originalFileName =  lostImg.getOriginalFilename();
        String imgUrl = "";
        String imgName = "";

        if(!StringUtils.isEmpty(originalFileName)){
            imgName = fileService.uploadLostFile(lostImgLocation, originalFileName, lostImg.getBytes());
            imgUrl = "/images/lost/" + imgName;
        }

        lost.setLostOriName(originalFileName);
        lost.setLostImgUrl(imgUrl);
        lost.setLostFileName(imgName);

        lostRepository.save(lost);

        return lost.getLostNo();
    }

    public void deleteLost(Long lostNo){
        Lost lost = lostRepository.findByLostNo(lostNo);

        lostRepository.delete(lost);
    }

    public boolean lostValidation(String userName, Long lostNo){
        User user = userRepository.findByUserId(userName);
        if(user == null) {
            System.out.println("--------------> 카카오 유저 ");
            user = userRepository.findByUserEmail(userName);
        }
        Lost lost = lostRepository.findById(lostNo).orElseThrow(EntityNotFoundException::new);
        return Objects.equals(lost.getUser().getUserNo(), user.getUserNo());
    }
}
