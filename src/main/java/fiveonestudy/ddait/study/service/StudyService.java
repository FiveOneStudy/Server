package fiveonestudy.ddait.study.service;

import fiveonestudy.ddait.myPage.entity.CertificationStatus;
import fiveonestudy.ddait.study.dto.*;
import fiveonestudy.ddait.study.entity.*;
import fiveonestudy.ddait.study.repository.*;
import fiveonestudy.ddait.user.repository.UserRepository;
import fiveonestudy.ddait.user.entity.User;
import fiveonestudy.ddait.myPage.entity.CertificationStatus;
import fiveonestudy.ddait.myPage.repository.UserCertificationRepository;
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
    private final UserProgressRepository userProgressRepository;
    private final UserCertificationRepository userCertificationRepository;
    private final UserRepository userRepository;

    public StudyResponse getStudy(String userName) {

        Map<String, String> studyDateMap = studyRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Study::getName,
                        Study::getDate
                ));

        List<StudyResponse.MyStudy> myStudies =
                userStudyRepository.findByUserName(userName)
                        .stream()
                        .map(us -> {
                            String date = studyDateMap.get(us.getStudyName());
                            int dday = calculateDDay(date);

                            return new StudyResponse.MyStudy(
                                    us.getStudyName(),
                                    dday
                            );
                        })
                        .collect(Collectors.toList());

        List<String> allStudies = studyDateMap.keySet()
                .stream()
                .toList();

        return StudyResponse.builder()
                .study(myStudies)
                .allStudy(allStudies)
                .build();
    }

    private int calculateDDay(String dateStr) {
        LocalDate today = LocalDate.now();
        LocalDate examDate = LocalDate.parse(dateStr); // "yyyy-MM-dd"

        return (int) ChronoUnit.DAYS.between(today, examDate);

    }

    public StudyJoinResponse joinStudy(String userName, StudyNameRequest request) {

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

    public StudyTipResponse insertTip(String nickname, StudyTipInsertRequest request) {

        StudyTip tip = StudyTip.builder()
                .studyName(request.getStudyName())
                .title(request.getTitle())
                .writer(nickname)
                .createdDate(LocalDate.now())
                .content(request.getContent())
                .url(request.getUrl())
                .build();

        studyTipRepository.save(tip);

        return StudyTipResponse.builder()
                .answer(true)
                .build();
    }

    public StudyTipReadResponse readTip(String nickname, StudyTipReadRequest request) {

        StudyTip tip = studyTipRepository.findById(request.getStudyId())
                .orElseThrow(() -> new RuntimeException("해당 글이 없습니다."));

        boolean isOwner = tip.getWriter().equals(nickname);

        String writerProfileImage = userRepository.findByNickname(tip.getWriter())
                .map(u -> "/mypage/profile-image/" + u.getId())
                .orElse(null);

        return StudyTipReadResponse.builder()
                .title(tip.getTitle())
                .writer(tip.getWriter())
                .profileImage(writerProfileImage)
                .date(tip.getCreatedDate())
                .content(tip.getContent())
                .url(tip.getUrl())
                .button(isOwner)
                .build();
    }

    public StudyTipListResponse getTips(StudyNameRequest requestDto) {

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

    private UserProgress getOrCreateUserProgress(StudyProgress study, User user) {
        return study.getUserProgressList().stream()
                .filter(u -> u.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseGet(() -> {
                    UserProgress newUser = UserProgress.builder()
                            .nickname(user.getNickname())
                            .user(user)
                            .studyProgress(study)
                            .build();

                    List<UserMission> userMissions = study.getMissions().stream()
                            .map(mission -> UserMission.builder()
                                    .userProgress(newUser)
                                    .studyMission(mission)
                                    .completed(false)
                                    .build()
                            )
                            .toList();

                    newUser.setUserMissions(userMissions);

                    UserProgress savedUser = userProgressRepository.save(newUser);
                    study.getUserProgressList().add(savedUser);

                    return savedUser;
                });
    }

    public StudyProgressResponse completeMission(String email, String studyName, String subject) {

        StudyProgress study = studyProgressRepository.findByStudyName(studyName)
                .orElseThrow(() -> new RuntimeException("스터디 없음"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        UserProgress me = getOrCreateUserProgress(study, user);

        UserMission targetMission = me.getUserMissions().stream()
                .filter(um -> um.getStudyMission().getMissionName().equals(subject))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("미션 없음"));

        targetMission.setCompleted(!targetMission.isCompleted());

        return buildResponse(study, me);
    }

    public StudyProgressResponse getProgress(String email, String studyName) {

        StudyProgress study = studyProgressRepository.findByStudyName(studyName)
                .orElseThrow(() -> new RuntimeException("스터디 없음"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        UserProgress me = getOrCreateUserProgress(study, user);

        return buildResponse(study, me);
    }

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
                        "/mypage/profile-image/" + u.getUser().getId(), // 바로 접근, 맵 불필요
                        String.valueOf(progressMap.get(u))
                ))
                .toList();

        List<List<Object>> mission = me.getUserMissions().stream()
                .map(um -> List.of(
                        (Object) um.getStudyMission().getMissionName(),
                        (Object) um.isCompleted()
                ))
                .toList();

        String myProfileImageUrl = "/mypage/profile-image/" + me.getUser().getId();

        return StudyProgressResponse.builder()
                .mainProgress(mainProgress)
                .memberProgress(memberProgress)
                .name(me.getNickname())
                .profileImage(myProfileImageUrl)
                .progress(String.valueOf(progressMap.get(me)))
                .mission(mission)
                .build();
    }

    public MissionSearchResponse searchMission(String email, String studyName, String keyword) {

        StudyProgress study = studyProgressRepository.findByStudyName(studyName)
                .orElseThrow(() -> new RuntimeException("스터디 없음"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        UserProgress me = getOrCreateUserProgress(study, user);

        List<List<Object>> mission = me.getUserMissions().stream()
                .filter(um -> um.getStudyMission().getMissionName().contains(keyword))
                .map(um -> List.of(
                        (Object) um.getStudyMission().getMissionName(),
                        (Object) um.isCompleted()
                ))
                .toList();

        return MissionSearchResponse.builder()
                .mission(mission)
                .build();
    }

    public StudyJoinResponse deleteTip(String email, StudyTipReadRequest request) {

        StudyTip tip = studyTipRepository.findById(request.getStudyId())
                .orElseThrow(() -> new RuntimeException("삭제할 해당 글이 없습니다."));
        studyTipRepository.delete(tip);

        return StudyJoinResponse.builder()
                .answer(true)
                .build();
    }

    public boolean checkBadge(String email, BadgeCheckRequest request) {

        boolean hasBadge = userCertificationRepository.existsActiveCertificationName(
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("유저 없음"))
                        .getId(),
                request.getStudyName(),
                List.of(CertificationStatus.APPROVED)
        );

        return hasBadge;
    }

}