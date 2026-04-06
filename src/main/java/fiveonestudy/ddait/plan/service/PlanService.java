package fiveonestudy.ddait.plan.service;

import fiveonestudy.ddait.plan.entity.*;
import fiveonestudy.ddait.plan.dto.*;
import fiveonestudy.ddait.plan.repository.CheckListRepository;
import fiveonestudy.ddait.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final CheckListRepository checkListRepository;

    public PlanResponse getPlan(String email, LocalDate date) {

        List<Plan> dailyPlans = planRepository.findByEmailAndDate(email, date);

        List<String> planList = dailyPlans.stream()
                .map(Plan::getPlanContent)
                .toList();

        List<CheckList> checks = checkListRepository.findByEmailAndDate(email, date);

        List<CheckItem> checkList = checks.stream()
                .map(c -> new CheckItem(c.getCheckContent(), c.isCompleted()))
                .toList();

        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());

        List<Plan> monthlyPlans = planRepository.findByEmailAndDateBetween(email, start, end);

        Map<LocalDate, List<String>> map = new HashMap<>();

        for (Plan plan : monthlyPlans) {
            map.computeIfAbsent(plan.getDate(), k -> new ArrayList<>())
                    .add(plan.getPlanContent());
        }

        List<MonthlyPlan> monthPlans = map.entrySet().stream()
                .map(e -> new MonthlyPlan(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(MonthlyPlan::getDate))
                .toList();

        return new PlanResponse(monthPlans, planList, checkList);
    }

    public PlanResponse insertMonthlyPlan(String email, PlanMonthInsertRequest requestDto) {

        Plan plan = Plan.builder()
                .email(email)
                .date(requestDto.getDate())
                .planContent(requestDto.getContent())
                .build();

        planRepository.save(plan);

        return getPlan(email, requestDto.getDate());
    }

    public PlanResponse updateMonthlyPlan(String email, PlanMonthUpdateRequest requestDto) {

        Plan plan = planRepository
                .findByEmailAndDateAndPlanContent(
                        email,
                        requestDto.getDate(),
                        requestDto.getOldContent()
                )
                .orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다."));

        Plan updatedPlan = Plan.builder()
                .planId(plan.getPlanId())
                .email(plan.getEmail())
                .date(plan.getDate())
                .planContent(requestDto.getNewContent())
                .build();

        planRepository.save(updatedPlan);

        return getPlan(email, requestDto.getDate());
    }

    public PlanResponse deleteMonthlyPlan(String email, PlanMonthInsertRequest requestDto) {

        Plan plan = planRepository
                .findByEmailAndDateAndPlanContent(
                        email,
                        requestDto.getDate(),
                        requestDto.getContent()
                )
                .orElseThrow(() -> new RuntimeException("삭제할 일정이 없습니다."));

        planRepository.delete(plan);

        return getPlan(email, requestDto.getDate());
    }

    public PlanResponse insertCheckList(String email, CheckListInsertRequest requestDto) {

        CheckList check = CheckList.builder()
                .email(email)
                .date(requestDto.getDate())
                .checkContent(requestDto.getContent())
                .completed(false)
                .build();

        checkListRepository.save(check);

        return getPlan(email, requestDto.getDate());
    }

    public PlanResponse updateCheckList(String email, CheckListUpdateRequest requestDto) {

        CheckList check = checkListRepository
                .findByEmailAndDateAndCheckContent(
                        email,
                        requestDto.getDate(),
                        requestDto.getOldContent()
                )
                .orElseThrow(() -> new RuntimeException("해당 체크리스트가 없습니다."));

        CheckList updated = CheckList.builder()
                .checkId(check.getCheckId())
                .email(check.getEmail())
                .date(check.getDate())
                .checkContent(requestDto.getNewContent())
                .completed(check.isCompleted())
                .build();

        checkListRepository.save(updated);

        return getPlan(email, requestDto.getDate());
    }

    public PlanResponse deleteCheckList(String email, CheckListInsertRequest requestDto) {

        CheckList check = checkListRepository
                .findByEmailAndDateAndCheckContent(
                        email,
                        requestDto.getDate(),
                        requestDto.getContent()
                )
                .orElseThrow(() -> new RuntimeException("삭제할 체크리스트가 없습니다."));

        checkListRepository.delete(check);

        return getPlan(email, requestDto.getDate());
    }

    public PlanResponse completeCheckList(String email, CheckListCompleteRequest requestDto) {

        CheckList check = checkListRepository
                .findByEmailAndDateAndCheckContent(
                        email,
                        requestDto.getDate(),
                        requestDto.getContent()
                )
                .orElseThrow(() -> new RuntimeException("해당 체크리스트가 없습니다."));

        check.updateCompleted(true);

        checkListRepository.save(check);

        return getPlan(email, requestDto.getDate());
    }
}