package fiveonestudy.ddait.study.controller;

import fiveonestudy.ddait.jwt.service.JwtService;
import fiveonestudy.ddait.study.dto.StudyJoinRequest;
import fiveonestudy.ddait.study.dto.StudyJoinResponse;
import fiveonestudy.ddait.study.dto.StudyResponse;
import fiveonestudy.ddait.study.service.StudyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final JwtService jwtService;

    @GetMapping
    public StudyResponse getStudy(HttpServletRequest request) {

        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("토큰에서 이메일을 추출할 수 없습니다."));

        return studyService.getStudy(email);
    }

    @PostMapping("/in")
    public StudyJoinResponse joinStudy(
            HttpServletRequest request,
            @RequestBody StudyJoinRequest requestDto
    ) {

        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("토큰에서 이메일을 추출할 수 없습니다."));

        return studyService.joinStudy(email, requestDto);
    }
}