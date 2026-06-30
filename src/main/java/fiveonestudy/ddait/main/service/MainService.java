package fiveonestudy.ddait.main.service;

import fiveonestudy.ddait.community.repository.PostRepository;
import fiveonestudy.ddait.main.dto.CheckCompleteRequest;
import fiveonestudy.ddait.main.dto.CommunitySearchResponse;
import fiveonestudy.ddait.main.dto.SearchResponse;
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
import fiveonestudy.ddait.study.repository.StudyTipRepository;
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
    private final StudyTipRepository studyTipRepository;
    private final UserStudyRepository userStudyRepository;
    private final UserCertificationRepository userCertificationRepository;
    private final PostRepository postRepository; // 의존성 추가

    @Transactional(readOnly = true)
    public CommunitySearchResponse searchCommunity(String keyword) {

        List<List<Object>> posts = postRepository.findByTitleContaining(keyword)
                .stream()
                .map(post -> Arrays.<Object>asList(
                        post.getId(),
                        post.getViewCount(),
                        post.getTitle()
                ))
                .toList();

        return CommunitySearchResponse.builder()
                .post(posts)
                .build();
    }

    @Transactional(readOnly = true)
    public SearchResponse searchStudy(String keyword) {

        List<String> studies = studyRepository.findByNameContaining(keyword)
                .stream()
                .map(Study::getName)
                .toList();

        List<List<Object>> tips = studyTipRepository.findByTitleContaining(keyword)
                .stream()
                .map(tip -> Arrays.asList((Object)tip.getId(), (Object)tip.getTitle()))
                .toList();

        return SearchResponse.builder()
                .study(studies)
                .tips(tips)
                .build();
    }

    @Transactional
    public MainResponse updateCheckAndGetMainData(String email, CheckCompleteRequest requestDto) {

        CheckList check = checkListRepository
                .findByEmailAndDateAndCheckContent(
                        email,
                        requestDto.getDate(),
                        requestDto.getContent()
                )
                .orElseThrow(() -> new RuntimeException("해당 체크리스트를 찾을 수 없습니다."));

        check.updateCompleted(true);

        checkListRepository.save(check);

        return getMainData(email, requestDto.getDate());
    }

    public MainResponse getMainData(String email, LocalDate date) {

        List<Plan> dailyPlans = planRepository.findByEmailAndDate(email, date);
        List<String> planList = dailyPlans.stream().map(Plan::getPlanContent).toList();

        List<CheckList> checks = checkListRepository.findByEmailAndDate(email, date);
        List<CheckItem> checkList = checks.stream()
                .map(c -> new CheckItem(c.getCheckContent(), c.isCompleted()))
                .toList();

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

        Map<String, String> studyDateMap = studyRepository.findAll().stream()
                .collect(Collectors.toMap(Study::getName, Study::getDate));

        List<StudyResponse.MyStudy> myStudies = userStudyRepository.findByUserName(email)
                .stream()
                .map(us -> {
                    String examDate = studyDateMap.get(us.getStudyName());
                    int dday = (int) ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(examDate));
                    return new StudyResponse.MyStudy(us.getStudyName(), dday);
                }).toList();

        List<String> certifications = userCertificationRepository.findByUserEmailWithCertification(email)
                .stream()
                .map(uc -> uc.getCertification().getName())
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