package fiveonestudy.ddait.main.controller;

import fiveonestudy.ddait.jwt.service.JwtService;
import fiveonestudy.ddait.main.dto.CheckCompleteRequest;
import fiveonestudy.ddait.main.dto.MainRequest;
import fiveonestudy.ddait.main.dto.MainResponse;
import fiveonestudy.ddait.main.service.MainService;
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
}