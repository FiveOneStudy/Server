package fiveonestudy.ddait.study.service;

import fiveonestudy.ddait.study.dto.*;
import fiveonestudy.ddait.study.entity.*;
import fiveonestudy.ddait.study.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserStudyRepository userStudyRepository;
    private final StudyRequestRepository studyRequestRepository;
    private final StudyTipRepository studyTipRepository;
    private final StudyProgressRepository studyProgressRepository;
    private final UserMissionRepository userMissionRepository;

    public StudyResponse getStudy(String userName) {

        // studyName → date 매핑
        Map<String, String> studyDateMap = studyRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Study::getName,
                        Study::getDate
                ));

        // 내가 가입한 스터디 + dday 계산
        List<StudyResponse.MyStudy> myStudies =
                userStudyRepository.findByUserName(userName)
                        .stream()
                        .map(us -> {
                            String date = studyDateMap.get(us.getStudyName());
                            int dday = calculateDday(date);

                            return new StudyResponse.MyStudy(
                                    us.getStudyName(),
                                    dday
                            );
                        })
                        .collect(Collectors.toList());

        // 전체 스터디 목록
        List<String> allStudies = studyDateMap.keySet()
                .stream()
                .toList();

        return StudyResponse.builder()
                .study(myStudies)
                .allStudy(allStudies)
                .build();
    }

    // 🔥 숫자 D-day 반환
    private int calculateDday(String dateStr) {
        LocalDate today = LocalDate.now();
        LocalDate examDate = LocalDate.parse(dateStr); // "yyyy-MM-dd"

        return (int) ChronoUnit.DAYS.between(today, examDate);
        // 미래: 양수 (남은 일수)
        // 오늘: 0
        // 과거: 음수
    }

    public StudyJoinResponse joinStudy(String userName, StudyNameRequest request) {

        // 🔥 중복 가입 방지
        boolean exists = userStudyRepository
                .findByUserNameAndStudyName(userName, request.getStudyName())
                .isPresent();

        if (!exists) {
            UserStudy userStudy = UserStudy.builder()
                    .userName(userName)
                    .studyName(request.getStudyName())
                    .build();

            userStudyRepository.save(userStudy);
        }

        return StudyJoinResponse.builder()
                .answer(true)
                .build();
    }

    public StudyJoinResponse leaveStudy(String userName, StudyNameRequest request) {

        userStudyRepository.deleteByUserNameAndStudyName(
                userName,
                request.getStudyName()
        );

        return StudyJoinResponse.builder()
                .answer(true)
                .build();
    }

    public StudyJoinResponse requestStudy(String userName, StudyNameRequest request) {

        boolean exists = studyRequestRepository
                .findByUserNameAndStudyName(userName, request.getStudyName())
                .isPresent();

        if (!exists) {
            StudyRequest studyRequest = StudyRequest.builder()
                    .userName(userName)
                    .studyName(request.getStudyName())
                    .build();

            studyRequestRepository.save(studyRequest);
        }

        return StudyJoinResponse.builder()
                .answer(true)
                .build();
    }

    public StudyTipResponse insertTip(String email, StudyTipInsertRequest request) {

        StudyTip tip = StudyTip.builder()
                .studyName(request.getStudyName())
                .title(request.getTitle())
                .writer(request.getWriter())
                .createdDate(LocalDate.now())
                .content(request.getContent())
                .url(request.getUrl())
                .build();

        studyTipRepository.save(tip);

        return StudyTipResponse.builder()
                .answer(true)
                .build();
    }

    public StudyTipReadResponse readTip(String email, StudyTipReadRequest request) {

        StudyTip tip = studyTipRepository.findById(request.getStudyId())
                .orElseThrow(() -> new RuntimeException("해당 글이 없습니다."));

        return StudyTipReadResponse.builder()
                .title(tip.getTitle())
                .writer(tip.getWriter())
                .date(tip.getCreatedDate())
                .content(tip.getContent())
                .url(tip.getUrl())
                .build();
    }

    public StudyTipListResponse getTips(String email, StudyNameRequest requestDto) { // 파라미터 수정

        List<StudyTip> tips = studyTipRepository.findByStudyName(requestDto.getStudyName());

        List<List<Object>> tipList = tips.stream()
                .map(tip -> Arrays.<Object>asList(
                        tip.getId(),
                        tip.getTitle(),
                        tip.getWriter(),
                        tip.getCreatedDate() != null ? tip.getCreatedDate().toString() : ""
                ))
                .collect(Collectors.toList());

        return new StudyTipListResponse(tipList);
    }

    public StudyProgressResponse completeMission(String nickname, String studyName, String subject) {

        StudyProgress study = studyProgressRepository.findByStudyName(studyName)
                .orElseThrow(() -> new RuntimeException("스터디 없음"));

        // 🔹 유저 찾기
        UserProgress me = study.getUserProgressList().stream()
                .filter(u -> u.getNickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        // 🔹 미션 찾기
        UserMission targetMission = me.getUserMissions().stream()
                .filter(um -> um.getStudyMission().getMissionName().equals(subject))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("미션 없음"));

        // 🔥 완료 처리
        targetMission.setCompleted(true);

        return buildResponse(study, me);
    }

    public StudyProgressResponse getProgress(String nickname, String studyName) {

        StudyProgress study = studyProgressRepository.findByStudyName(studyName)
                .orElseThrow(() -> new RuntimeException("스터디 없음"));

        UserProgress me = study.getUserProgressList().stream()
                .filter(u -> u.getNickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        return buildResponse(study, me);
    }

    // 🔥 공통 로직 분리 (중요)
    private StudyProgressResponse buildResponse(StudyProgress study, UserProgress me) {

        List<UserProgress> users = study.getUserProgressList();

        Map<UserProgress, Integer> progressMap = new HashMap<>();

        for (UserProgress user : users) {
            List<UserMission> missions = user.getUserMissions();

            int total = missions.size();
            long completed = missions.stream()
                    .filter(UserMission::isCompleted)
                    .count();

            int progress = total == 0 ? 0 : (int)((double) completed / total * 100);

            progressMap.put(user, progress);
        }

        int mainProgress = (int) progressMap.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);

        List<List<String>> memberProgress = users.stream()
                .map(u -> List.of(
                        u.getNickname(),
                        String.valueOf(progressMap.get(u))
                ))
                .toList();

        List<List<Object>> mission = me.getUserMissions().stream()
                .map(um -> List.of(
                        (Object) um.getStudyMission().getMissionName(),
                        (Object) um.isCompleted()
                ))
                .toList();

        return StudyProgressResponse.builder()
                .mainProgress(mainProgress)
                .memberProgress(memberProgress)
                .name(me.getNickname())
                .progress(String.valueOf(progressMap.get(me)))
                .mission(mission)
                .build();
    }
}