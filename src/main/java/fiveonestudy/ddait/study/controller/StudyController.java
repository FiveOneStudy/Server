package fiveonestudy.ddait.study.controller;

import fiveonestudy.ddait.jwt.service.JwtService;
import fiveonestudy.ddait.study.dto.*;
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
            @RequestBody StudyNameRequest requestDto
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

    @DeleteMapping("/out")
    public StudyJoinResponse leaveStudy(
            HttpServletRequest request,
            @RequestBody StudyNameRequest requestDto
    ) {

        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("토큰에서 이메일을 추출할 수 없습니다."));

        return studyService.leaveStudy(email, requestDto);
    }

    @PostMapping("/request")
    public StudyJoinResponse requestStudy(
            HttpServletRequest request,
            @RequestBody StudyNameRequest requestDto
    ) {

        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("토큰에서 이메일을 추출할 수 없습니다."));

        return studyService.requestStudy(email, requestDto);
    }

    @PostMapping("/tip/insert")
    public StudyTipResponse insertTip(
            HttpServletRequest request,
            @RequestBody StudyTipInsertRequest requestDto
    ) {

        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("토큰에서 이메일을 추출할 수 없습니다."));

        return studyService.insertTip(email, requestDto);
    }

    @PostMapping("/tip/read")
    public StudyTipReadResponse readTip(
            HttpServletRequest request,
            @RequestBody StudyTipReadRequest requestDto
    ) {

        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("토큰에서 이메일을 추출할 수 없습니다."));

        return studyService.readTip(email, requestDto);
    }

    @PostMapping("/tip")
    public StudyTipListResponse getTips(
            HttpServletRequest request,
            @RequestBody StudyNameRequest requestDto
    ) {
        // 1. 토큰 추출 및 검증
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        // 2. 이메일 추출
        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("토큰에서 이메일을 추출할 수 없습니다."));

        // 3. 서비스 호출 (email과 requestDto를 모두 전달)
        return studyService.getTips(email, requestDto);
    }

    @PostMapping("/progress")
    public StudyProgressResponse getProgress(
            HttpServletRequest request,
            @RequestBody StudyNameRequest requestDto,
            @RequestParam String nickname
    ) {
        // 1. 토큰 추출
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        // 2. 토큰 검증
        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        // 3. 서비스 호출
        return studyService.getProgress(nickname, requestDto.getStudyName());
    }

    @PostMapping("/progress/complete")
    public StudyProgressResponse completeMission(
            HttpServletRequest request,
            @RequestParam String nickname,
            @RequestParam String studyName,
            @RequestBody MissionCompleteRequest requestDto
    ) {
        // 1. 토큰 추출
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        // 2. 토큰 검증
        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        // 3. 서비스 호출
        return studyService.completeMission(
                nickname,
                studyName,
                requestDto.getSubject()
        );
    }
}