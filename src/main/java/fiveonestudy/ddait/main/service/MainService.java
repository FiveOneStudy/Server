package fiveonestudy.ddait.main.service;

import fiveonestudy.ddait.myPage.repository.UserCertificationRepository;
import fiveonestudy.ddait.plan.dto.CheckItem;
import fiveonestudy.ddait.plan.dto.MonthlyPlan;
import fiveonestudy.ddait.plan.entity.CheckList;
import fiveonestudy.ddait.plan.entity.Plan;
import fiveonestudy.ddait.plan.repository.CheckListRepository;
import fiveonestudy.ddait.plan.repository.PlanRepository;
import fiveonestudy.ddait.study.dto.StudyResponse;
import fiveonestudy.ddait.study.entity.Study;
import fiveonestudy.ddait.study.repository.StudyRepository;
import fiveonestudy.ddait.study.repository.UserStudyRepository;
import fiveonestudy.ddait.main.dto.MainResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainService {

    private final PlanRepository planRepository;
    private final CheckListRepository checkListRepository;
    private final StudyRepository studyRepository;
    private final UserStudyRepository userStudyRepository;
    private final UserCertificationRepository userCertificationRepository;

    public MainResponse getMainData(String email, LocalDate date) {

        // 1. 플랜 및 체크리스트 조회 (PlanService 로직)
        List<Plan> dailyPlans = planRepository.findByEmailAndDate(email, date);
        List<String> planList = dailyPlans.stream().map(Plan::getPlanContent).toList();

        List<CheckList> checks = checkListRepository.findByEmailAndDate(email, date);
        List<CheckItem> checkList = checks.stream()
                .map(c -> new CheckItem(c.getCheckContent(), c.isCompleted()))
                .toList();

        // 월간 플랜 맵핑
        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
        List<Plan> monthlyPlans = planRepository.findByEmailAndDateBetween(email, start, end);

        Map<LocalDate, List<String>> planMap = new HashMap<>();
        for (Plan plan : monthlyPlans) {
            planMap.computeIfAbsent(plan.getDate(), k -> new ArrayList<>()).add(plan.getPlanContent());
        }
        List<MonthlyPlan> monthPlans = planMap.entrySet().stream()
                .map(e -> new MonthlyPlan(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(MonthlyPlan::getDate))
                .toList();

        // 2. 스터디 및 D-Day 조회 (StudyService 로직)
        Map<String, String> studyDateMap = studyRepository.findAll().stream()
                .collect(Collectors.toMap(Study::getName, Study::getDate));

        List<StudyResponse.MyStudy> myStudies = userStudyRepository.findByUserName(email)
                .stream()
                .map(us -> {
                    String examDate = studyDateMap.get(us.getStudyName());
                    int dday = (int) ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(examDate));
                    return new StudyResponse.MyStudy(us.getStudyName(), dday);
                }).toList();

        // 3. 자격증 이름 조회 (요청하신 Repository 메서드 활용)
        List<String> certifications = userCertificationRepository.findByUserEmailWithCertification(email)
                .stream()
                .map(uc -> uc.getCertification().getName()) // Certification 엔티티에서 name만 추출
                .toList();

        return MainResponse.builder()
                .study(myStudies)
                .monthPlans(monthPlans)
                .planList(planList)
                .checkList(checkList)
                .certification(certifications)
                .build();
    }
}