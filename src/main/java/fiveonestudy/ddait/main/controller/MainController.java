package fiveonestudy.ddait.main.controller;

import fiveonestudy.ddait.jwt.service.JwtService;
import fiveonestudy.ddait.main.dto.MainRequest;
import fiveonestudy.ddait.main.dto.MainResponse;
import fiveonestudy.ddait.main.service.MainService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
}