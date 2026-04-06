package fiveonestudy.ddait.plan.controller;

import fiveonestudy.ddait.plan.dto.PlanResponse;
import fiveonestudy.ddait.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public PlanResponse getPlan(
            @RequestParam String email,
            @RequestParam LocalDate date
    ) {
        return planService.getPlan(email, date);
    }
}