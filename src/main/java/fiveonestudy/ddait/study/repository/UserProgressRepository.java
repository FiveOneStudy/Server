package fiveonestudy.ddait.study.repository;

import fiveonestudy.ddait.study.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    Optional<UserProgress> findByNicknameAndStudyProgress(String nickname, StudyProgress study);
}