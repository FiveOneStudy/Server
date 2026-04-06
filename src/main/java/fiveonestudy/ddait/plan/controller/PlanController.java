package fiveonestudy.ddait.plan.controller;

import fiveonestudy.ddait.jwt.service.JwtService;
import fiveonestudy.ddait.plan.dto.PlanMonthInsertRequest;
import fiveonestudy.ddait.plan.dto.PlanMonthUpdateRequest;
import fiveonestudy.ddait.plan.dto.PlanRequest;
import fiveonestudy.ddait.plan.dto.PlanResponse;
import fiveonestudy.ddait.plan.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final JwtService jwtService;

    @PostMapping
    public PlanResponse getPlan(
            HttpServletRequest request,
            @RequestBody PlanRequest planRequest
    ) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("토큰에서 이메일을 추출할 수 없습니다."));

        return planService.getPlan(email, planRequest.getDate());
    }

    @PostMapping("/month/insert")
    public PlanResponse insertMonthlyPlan(
            HttpServletRequest request,
            @RequestBody PlanMonthInsertRequest requestDto
    ) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("토큰에서 이메일을 추출할 수 없습니다."));

        return planService.insertMonthlyPlan(email, requestDto);
    }

    @PutMapping("/month/update")
    public PlanResponse updateMonthlyPlan(
            HttpServletRequest request,
            @RequestBody PlanMonthUpdateRequest requestDto
    ) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new RuntimeException("Access Token이 없습니다."));

        if (!jwtService.isTokenValid(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("토큰에서 이메일을 추출할 수 없습니다."));

        return planService.updateMonthlyPlan(email, requestDto);
    }


}