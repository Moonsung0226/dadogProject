package com.keduit.dadog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keduit.dadog.entity.Shelter;
import com.keduit.dadog.repository.ShelterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ShelterService {

    private static final Logger logger = LoggerFactory.getLogger(ShelterService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ShelterRepository shelterRepository;

    // 보호소 API에서 데이터를 가져오고 저장하는 메서드
    public void fetchAndSaveShelterData() {

        String serviceKey = "zkrOzDsSrpL4c11GCZfNdA3xfk4HHlDX3WUzUPkXtbhzzhCJp1izbMHWaA1lbLLzmmzR3f4NUloXKWN5LBpqNQ==";
        String url = "https://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic?serviceKey="
                + serviceKey + "&pageNo=1&numOfRows=10&_type=json";

        // 첫 번째 호출에서 totalCount 가져오기
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        logger.info("API 호출: {}", url); // API 호출 확인

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            logger.info("API 응답: {}", responseBody); // API 응답 내용 출력

            // 응답이 JSON인지 확인
            if (responseBody.startsWith("{") || responseBody.startsWith("[")) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode root = objectMapper.readTree(responseBody);
                    JsonNode body = root.path("response").path("body");
                    int totalCount = body.path("totalCount").asInt(); // totalCount 가져오기
                    logger.info("totalCount: {}", totalCount); // totalCount 출력

                    int numOfRows = 10; // 한 번에 가져올 행 수
                    int totalPages = (int) Math.ceil((double) totalCount / numOfRows); // 전체 페이지 수 계산
                    logger.info("전체 페이지 수: {}", totalPages);

                    // 모든 페이지를 순회하며 데이터 가져오기
                    for (int pageNo = 1; pageNo <= totalPages; pageNo++) {
                        String pagedUrl = "https://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic?serviceKey="
                                + serviceKey + "&pageNo=" + pageNo + "&numOfRows=" + numOfRows + "&_type=json";

                        ResponseEntity<String> pagedResponse = restTemplate.getForEntity(pagedUrl, String.class);
                        logger.info("페이지 {} API 호출: {}", pageNo, pagedUrl); // 각 페이지 호출 로그

                        if (pagedResponse.getStatusCode() == HttpStatus.OK) {
                            String pagedResponseBody = pagedResponse.getBody();
                            JsonNode pagedRoot = objectMapper.readTree(pagedResponseBody);
                            JsonNode items = pagedRoot.path("response").path("body").path("items").path("item");

                            // items 배열 확인
                            if (items.isArray()) {
                                // items 배열 순회하며 엔티티에 저장
                                for (JsonNode item : items) {
                                    String careAddr = item.path("careAddr").asText();
                                    String orgdownNm = item.path("orgdownNm").asText();
                                    String careNm = item.path("careNm").asText();

                                    // shelCity 설정: careAddr에서 첫 번째 공백 이전의 단어를 가져옴
                                    String shelCity = careAddr.contains(" ") ? careAddr.substring(0, careAddr.indexOf(" ")) : careAddr;

                                    logger.info("careAddr: {}, orgdownNm: {}", careAddr, orgdownNm); // 각 데이터 확인

                                    // 조건 확인 및 중복 체크
                                    if (careAddr.contains(orgdownNm)) {
                                        logger.info("조건 충족: careAddr에 orgdownNm 포함");

                                        // 중복된 careNm 확인
                                        if (shelterRepository.findByShelNm(careNm) == null) {
                                            Shelter shelter = new Shelter();
                                            shelter.setShelNm(careNm);
                                            shelter.setShelAddr(careAddr);
                                            shelter.setShelCity(shelCity); // shelCity 설정

                                            logger.info("Shelter 엔티티 생성: {}", shelter); // 엔티티 값 확인

                                            // 엔티티 저장
                                            shelterRepository.save(shelter);
                                            logger.info("Shelter 저장 완료: {}", shelter); // 저장 완료 확인
                                        } else {
                                            logger.info("중복된 careNm 발견, 저장하지 않음: {}", careNm);
                                        }
                                    } else {
                                        logger.info("조건 불충족: careAddr에 orgdownNm 미포함");
                                    }
                                }
                            } else {
                                logger.warn("items가 배열이 아닙니다: {}", items);
                            }
                        } else {
                            logger.error("페이지 {} API 호출 실패: {} - 응답 코드: {}", pageNo, pagedResponse.getStatusCode(), pagedResponse.getBody());
                        }
                    }
                } catch (Exception e) {
                    logger.error("Shelter 데이터 처리 중 오류 발생", e);
                }
            } else {
                logger.error("예상치 못한 응답 형식: HTML 응답 수신");
            }
        } else {
            logger.error("API 호출 실패: {} - 응답 코드: {}", response.getStatusCode(), response.getBody());
        }
    }

    // 특정 검색어로 보호소를 검색하는 메서드
    public Page<Shelter> searchShelters(String filter, String searchTerm, Pageable pageable) {
        // 필터에 따라 검색어로 보호소 검색
        if ("name".equals(filter)) {
            return shelterRepository.findByShelNmContaining(searchTerm.trim(), pageable);
        } else if ("city".equals(filter)) {
            return shelterRepository.findByShelCityContaining(searchTerm.trim(), pageable);
        } else if ("all".equals(filter)) {
            return shelterRepository.findByShelNmContainingOrShelCityContaining(searchTerm.trim(), searchTerm.trim(), pageable);
        }
        return shelterRepository.findAll(pageable); // 기본적으로 모든 보호소 반환
    }
}
