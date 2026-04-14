package fiveonestudy.ddait.main.controller;

import fiveonestudy.ddait.jwt.service.JwtService;
import fiveonestudy.ddait.main.dto.*;
import fiveonestudy.ddait.main.service.MainService;
import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;
    private final JwtService jwtService;

    @PostMapping("/main")
    public MainResponse getMainData(
            HttpServletRequest request,
            @RequestBody MainRequest requestDto
    ) {
        // StudyController와 동일한 토큰 검증 로직
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("토큰에서 이메일을 추출할 수 없습니다."));

        return mainService.getMainData(email, requestDto.getDate());
    }

    @PatchMapping("/main/check")
    public MainResponse completeCheck(
            HttpServletRequest request,
            @RequestBody CheckCompleteRequest requestDto
    ) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token 없음"));

        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("이메일 추출 실패"));

        // 체크 상태 변경 후, 변경된 데이터를 포함한 전체 메인 데이터 반환
        return mainService.updateCheckAndGetMainData(email, requestDto);
    }

    @PostMapping("/main/search")
    public SearchResponse searchMain(@RequestBody SearchRequest requestDto) {
        // "study"인 경우에만 검색 수행 (community는 현재 개발 전이므로 빈 리스트 혹은 기본 처리)
        if ("study".equals(requestDto.getSelect())) {
            return mainService.searchStudy(requestDto.getSearch());
        }

        // study가 아닐 경우 빈 응답 반환
        return SearchResponse.builder()
                .study(Collections.emptyList())
                .tips(Collections.emptyList())
                .build();
    }
}