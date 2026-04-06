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

        // 🔹 하루 plan
        List<Plan> dailyPlans = planRepository.findByEmailAndDate(email, date);

        List<String> planList = dailyPlans.stream()
                .map(Plan::getPlanContent)
                .toList();

        // 🔹 하루 checkList
        List<CheckList> checks = checkListRepository.findByEmailAndDate(email, date);

        List<CheckItem> checkList = checks.stream()
                .map(c -> new CheckItem(c.getCheckContent(), c.isCompleted()))
                .toList();

        // 🔹 월간 plan
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
}