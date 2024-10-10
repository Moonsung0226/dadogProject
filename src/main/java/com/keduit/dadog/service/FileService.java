package com.keduit.dadog.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

import java.io.File;

@Service
@Log4j2
public class FileService {

    // 사진마다 고유한 uuid 를 붙여서 폴더에 저장
    public String uploadLostFile(String uploadPath, String originalFileName, byte[] fileData) {
        // 중복되지 않는 고유의 값을 구성할 때 사용하는 범용 고유 식별자
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        System.out.println("파일 데이터 길이: " + (fileData != null ? fileData.length : 0));
        // 디렉토리 존재 여부 확인 및 생성
        File directory = new File(uploadPath);
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdirs(); // 디렉토리 생성
            if (!dirCreated) {
                System.out.println("디렉토리 생성 실패: " + uploadPath);
                return null; // 실패 시 null 반환
            }
        }
        try (FileOutputStream fos = new FileOutputStream(fileUploadFullUrl)) {
            fos.write(fileData);
        } catch (Exception e) {
            System.out.println("에러났음 --------");
            e.printStackTrace();
        }
        System.out.println("----------파일 생성 완료 in FileService ------------>" + savedFileName);
        System.out.println("----------파일 생성 완료 in FileService ------------> " + fileUploadFullUrl);
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        //저장된 파일의 경로를 이용하여 파일 객체를 생성
        File deleteFile = new File(filePath);

        //해당 파일이 있으면 삭제
        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일 삭제 완료");
        }else{
            log.info("파일이 존재하지 않습니다.");
        }
    }
}

