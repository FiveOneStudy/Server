package fiveonestudy.ddait.study.service;

import fiveonestudy.ddait.study.dto.*;
import fiveonestudy.ddait.study.entity.Study;
import fiveonestudy.ddait.study.entity.StudyRequest;
import fiveonestudy.ddait.study.entity.StudyTip;
import fiveonestudy.ddait.study.entity.UserStudy;
import fiveonestudy.ddait.study.repository.StudyRepository;
import fiveonestudy.ddait.study.repository.StudyRequestRepository;
import fiveonestudy.ddait.study.repository.StudyTipRepository;
import fiveonestudy.ddait.study.repository.UserStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserStudyRepository userStudyRepository;
    private final StudyRequestRepository studyRequestRepository;
    private final StudyTipRepository studyTipRepository;

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

    public StudyJoinResponse joinStudy(String userName, StudyJoinRequest request) {

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

    public StudyJoinResponse leaveStudy(String userName, StudyJoinRequest request) {

        userStudyRepository.deleteByUserNameAndStudyName(
                userName,
                request.getStudyName()
        );

        return StudyJoinResponse.builder()
                .answer(true)
                .build();
    }

    public StudyJoinResponse requestStudy(String userName, StudyJoinRequest request) {

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
                .writer(request.getWriter())
                .title(request.getTitle())
                .createdDate(LocalDate.now().toString())
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
}