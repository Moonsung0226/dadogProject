package com.keduit.dadog.service;

import com.keduit.dadog.dto.LostDTO;
import com.keduit.dadog.dto.SearchDTO;
import com.keduit.dadog.entity.Lost;
import com.keduit.dadog.entity.User;

import com.keduit.dadog.repository.LostRepository;
import com.keduit.dadog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;
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
    public Page<Lost> getLostList(SearchDTO searchDTO, Pageable pageable) {
        return lostRepository.getAdoptListPage(searchDTO, pageable);
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
            imgName = fileService.uploadFile(lostImgLocation, originalFileName, lostImg.getBytes());
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
        try {
            fileService.deleteFile(lostImgLocation + "/" + lost.getLostFileName());
        } catch (Exception e) {
            System.out.println("파일 서비스 삭제 에러");
            throw new RuntimeException(e);
        }
        lostRepository.delete(lost);
    }

    public void updateLostWithImg(LostDTO lostDTO, MultipartFile lostImg) throws Exception{
       Lost lost = lostRepository.findByLostNo(lostDTO.getLostNo());
       lost.updateLost(lostDTO);

       //기존 파일 삭제
       fileService.deleteFile(lostImgLocation + "/" + lost.getLostFileName());

       String originalFileName =  lostImg.getOriginalFilename();
       String imgUrl = "";
       String imgName = "";

       if(!StringUtils.isEmpty(originalFileName)){
            imgName = fileService.uploadFile(lostImgLocation, originalFileName, lostImg.getBytes());
            imgUrl = "/images/lost/" + imgName;
        }

       lost.updateImg(originalFileName, imgUrl, imgName);
       lostRepository.save(lost);
    }

    public void updateLostWithOutImg(LostDTO lostDTO){
        Lost lost = lostRepository.findByLostNo(lostDTO.getLostNo());
        lost.updateLost(lostDTO);
        lostRepository.save(lost);
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

    public List<Lost> findAllLost() {
        return lostRepository.findAll(); // 모든 Lost 엔티티를 반환
    }
}
