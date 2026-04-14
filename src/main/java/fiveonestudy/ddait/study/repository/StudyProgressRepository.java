package fiveonestudy.ddait.study.repository;

import fiveonestudy.ddait.study.entity.StudyProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyProgressRepository extends JpaRepository<StudyProgress, Long> {
    Optional<StudyProgress> findByStudyName(String studyName);
}