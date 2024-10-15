package com.keduit.dadog.dto;

import com.keduit.dadog.constant.AdoptWait;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTO {
    private Long appNo;                   // 신청 ID
    private Long userNo;                  // 신청자 ID
    private String userId;                // 신청자 ID (사용자 ID)
    private String userName;              // 신청자 이름
    private String userEmail;             // 신청자 이메일
    private String userTel;               // 신청자 전화번호
    private Long adoptNo;                 // 입양 신청한 유기견 ID
    private String adoptKind;             // 유기견 종류
    private String adoptAge;              // 유기견 나이
    private String adoptEdt;              // 공고 종료일
    private String adoptCareNm;           // 보호소 이름
    private String adoptCareAddr;         // 보호소 주소
    private String adoptCareTel;          // 보호소 전화번호
    private AdoptWait adoptWaitStatus;    // 신청 상태
}
