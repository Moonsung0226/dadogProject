package com.keduit.dadog.service;

import com.keduit.dadog.dto.LostDTO;
import com.keduit.dadog.entity.Lost;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.LostRepository;
import com.keduit.dadog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LostService {

    private final LostRepository lostRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Value("${lostImgLocation}")
    private String lostImgLocation;

    //실종신고 등록
    public Long addLost(LostDTO lostDTO, String userName, MultipartFile lostImg) throws Exception{
        User user = userRepository.findByUserId(userName);
        Lost lost = Lost.createLost(lostDTO, userName, user);
        //TODO : lostImgUrl, lostOriName, lostFileName 넣어야함
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
}
