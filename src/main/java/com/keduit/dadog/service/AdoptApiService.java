package com.keduit.dadog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keduit.dadog.Repository.AdoptRepository;
import com.keduit.dadog.constant.Current;
import com.keduit.dadog.entity.Adopt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class AdoptApiService {

    private static final Logger logger = LoggerFactory.getLogger(AdoptApiService.class);
    //본 주소
    private final String BASE_URL = "https://apis.data.go.kr/1543061/abandonmentPublicSrvc";
    private final String apiUri = "/abandonmentPublic";
    //서비스키 (decoding)
    private final String serviceKey2 = "?serviceKey=Dg9XpNoVNbVYRT2U9F4JzIirqPRwWcAcNbYhQUg/ofT0RMFxZQGusAtUYtdL8VHV7yRjPYmLNk2Hu4ScXHFGMw==";
    //강아지만 (417000번)
    private final String upKind = "&upkind=417000";
    //페이지 총1만개이고 1000개씩 10페이지 할거임
    private final String pageNum = "&pageNo=1";
    //페이지당 개수
    private final String numOfRows = "&numOfRows=1000";
    //받아올 타입
    private final String type = "&_type=json";

    LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

    // 10일 빼기
    LocalDate dateMinus10Days = today.minusDays(10);

    // 날짜 형식 지정
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    // 형식에 맞춰 날짜 출력
    String formattedDate = dateMinus10Days.format(formatter);
    String bgnde = "&bgnde=" + formattedDate;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdoptRepository adoptRepository;

    public void fetchAndSaveAdoptData(){
        String serviceKey = "Dg9XpNoVNbVYRT2U9F4JzIirqPRwWcAcNbYhQUg/ofT0RMFxZQGusAtUYtdL8VHV7yRjPYmLNk2Hu4ScXHFGMw==";
        String url = BASE_URL +
                apiUri +
                serviceKey2 +
                bgnde +
                upKind +
                pageNum +
                numOfRows +
                type;

        // 첫 번째 호출에서 totalCount 가져오기
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        logger.info("API 호출 : {}", url);

        if(response.getStatusCode() == HttpStatus.OK){
            String responseBody = response.getBody();
            logger.info("API 응답 : {}", responseBody);

            try{
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode body = root.path("response").path("body");
                int totalCount = body.path("totalCount").asInt();
                logger.info("totalCount: {}", totalCount);

                int numOfRows = 100;
                int totalPages = (int) Math.ceil((double) totalCount / numOfRows);
                logger.info("전체 페이지 수 : {}",totalPages);

                for(int pageNo = 1; pageNo <= 30; pageNo++){
                    String pagedUrl = "https://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic?serviceKey=" +
                            serviceKey + bgnde + upKind + "&pageNo=" + pageNo + "&numOfRows=" + numOfRows + "&_type=json";

                    ResponseEntity<String> pagedResponse = restTemplate.getForEntity(pagedUrl, String.class);
                    logger.info("페이지 {} API 호출: {}", pageNo, pagedUrl);//각 페이지 호출 로그

                    if(pagedResponse.getStatusCode() == HttpStatus.OK){
                        String pagedResponseBody = pagedResponse.getBody();
                        JsonNode pagedRoot = objectMapper.readTree(pagedResponseBody);
                        JsonNode items = pagedRoot.path("response").path("body").path("items").path("item");

                        if(items.isArray()){
                            for(JsonNode item : items){
                                String kindCd = item.path("kindCd").asText().substring(4);
                                String age = item.path("age").asText();
                                String careNm = item.path("careNm").asText();
                                String careAddr = item.path("careAddr").asText();
                                String careTel = item.path("careTel").asText();
                                String weight = item.path("weight").asText();
                                String specialMark = item.path("specialMark").asText();
                                String popfile = item.path("popfile").asText();
                                String noticeEdt = item.path("noticeEdt").asText();

                                Adopt adopt = new Adopt();
                                adopt.setAdoptKind(kindCd);
                                adopt.setAdoptAge(age);
                                adopt.setAdoptCareNm(careNm);
                                adopt.setAdoptImgUrl(popfile);
                                adopt.setAdoptEdt(noticeEdt);
                                adopt.setAdoptWeight(weight);
                                adopt.setAdoptSpecial(specialMark);
                                adopt.setAdoptCareTel(careTel);
                                adopt.setAdoptCareAddr(careAddr);
                                adopt.setCurrent(Current.Y);
                                //저장
                                adoptRepository.save(adopt);
                                logger.info("엔티티 저장 완료 : {}", adopt);

                            }
                        }else{
                            logger.warn("items가 배열이 아님");
                        }
                    }else{
                        logger.error("페이지 {} API 호출 실패: {}", pageNo, pagedResponse.getStatusCode());
                    }
                }
            }catch (Exception e){
                logger.error("처리중 오류 발생", e);
            }
        }else{
            logger.error("API 호출 실패 : {}", response.getStatusCode());
        }
    }
}