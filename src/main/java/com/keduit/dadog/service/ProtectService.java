package com.keduit.dadog.service;

import com.keduit.dadog.dto.ProtectDTO;
import com.keduit.dadog.dto.SearchDTO;
import com.keduit.dadog.entity.Protect;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.ProtectRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProtectService {

    private final ProtectRepository protectRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Value("${proImgLocation}")
    private String proImgLocation;

    //실종신고 게시판
    @Transactional(readOnly = true)
    public Page<Protect> getProtectList(SearchDTO searchDTO, Pageable pageable) {
        return protectRepository.getProtectListPage(searchDTO, pageable);
    }

    public Protect getProtect(Long proNo){
        return protectRepository.findById(proNo).orElseThrow(EntityNotFoundException::new);
    }

    //실종신고 등록
    public Long addProtect(ProtectDTO protectDTO, String userName, MultipartFile protectImg) throws Exception{
        User user = userRepository.findByUserId(userName);
        if(user == null) {
            System.out.println("--------------> 여기");
            user = userRepository.findByUserEmail(userName);
            System.out.println("---------------> " + user.getUserNo());
        }

        Protect protect = Protect.createProtect(protectDTO, userName, user);
        System.out.println("-------------->" + protect.getProTitle());
        System.out.println("---------------->" + protectImg);
        String originalFileName =  protectImg.getOriginalFilename();
        String imgUrl = "";
        String imgName = "";
        System.out.println("---------------------->"+ originalFileName);
        System.out.println("--------------------->");

        if(!StringUtils.isEmpty(originalFileName)){
            imgName = fileService.uploadFile(proImgLocation, originalFileName, protectImg.getBytes());
            imgUrl = "/images/protect/" + imgName;
        }

        protect.setProOriName(originalFileName);
        protect.setProImgUrl(imgUrl);
        protect.setProFileName(imgName);

        protectRepository.save(protect);

        return protect.getProNo();
    }

    public void deleteProtect(Long proNo){
        Protect protect = protectRepository.findByProNo(proNo);
        try {
            fileService.deleteFile(proImgLocation + "/" + protect.getProFileName());
        } catch (Exception e) {
            System.out.println("파일 서비스 삭제 에러");
            throw new RuntimeException(e);
        }
        protectRepository.delete(protect);
    }

    public void updateProtectWithImg(ProtectDTO protectDTO, MultipartFile protectImg) throws Exception{
        Protect protect = protectRepository.findByProNo(protectDTO.getProNo());
        protect.updateProtect(protectDTO);

        //기존 파일 삭제
        fileService.deleteFile(proImgLocation + "/" + protect.getProFileName());

        String originalFileName =  protectImg.getOriginalFilename();
        String imgUrl = "";
        String imgName = "";

        if(!StringUtils.isEmpty(originalFileName)){
            imgName = fileService.uploadFile(proImgLocation, originalFileName, protectImg.getBytes());
            imgUrl = "/images/protect/" + imgName;
        }

        protect.updateImg(originalFileName, imgUrl, imgName);
        protectRepository.save(protect);
    }

    public void updateProtectWithOutImg(ProtectDTO protectDTO){
        Protect protect = protectRepository.findByProNo(protectDTO.getProNo());
        protect.updateProtect(protectDTO);
        protectRepository.save(protect);
    }

      //유저의 보호중 글 조회
    public List<ProtectDTO> getUserProtect(Long userNo){
        User user = userRepository.findByUserNo(userNo);
        List<Protect> protectList = protectRepository.findByUser(user);
        List<ProtectDTO> protectDTOList = new ArrayList<>();
        for(Protect protect : protectList){
            ProtectDTO protectDTO = new ProtectDTO();
            protectDTO = protectDTO.myProtect(protect);
            protectDTOList.add(protectDTO);
        }
        return protectDTOList;
    }


    public boolean protectValidation(String userName, Long proNo){
        User user = userRepository.findByUserId(userName);
        if(user == null) {
            System.out.println("--------------> 카카오 유저 ");
            user = userRepository.findByUserEmail(userName);
        }
        Protect protect = protectRepository.findById(proNo).orElseThrow(EntityNotFoundException::new);
        return Objects.equals(protect.getUser().getUserNo(), user.getUserNo());
    }

    // 최근 등록된 보호중 6개
    public List<Protect> findTop6ByOrderByCreateTimeDesc() {
        return protectRepository.findTop6ByOrderByCreateTimeDesc();
    }

    // proNo로 게시글 찾기
    public Protect findByProtectNo(Long protectNo) {
        return protectRepository.findById(protectNo).orElseThrow(() -> new EntityNotFoundException("Protect not found with protectNo : " + protectNo));
    }

    public Page<Protect> getMyProtectPage(Long userNum, Pageable pageable) {
        Page<Protect> protectPage = protectRepository.getMyProtectPage(pageable, userNum);
        return protectPage;
    }
}
