package fiveonestudy.ddait.study.repository;

import fiveonestudy.ddait.study.entity.StudyTip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyTipRepository extends JpaRepository<StudyTip, Long> {
    List<StudyTip> findByTitleContaining(String title);
    List<StudyTip> findByStudyName(String studyName);
}