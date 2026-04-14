package fiveonestudy.ddait.study.repository;

import fiveonestudy.ddait.study.entity.StudyRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyRequestRepository extends JpaRepository<StudyRequest, Long> {

    Optional<StudyRequest> findByUserNameAndStudyName(String userName, String studyName);
}