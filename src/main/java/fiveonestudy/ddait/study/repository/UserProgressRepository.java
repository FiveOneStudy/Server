package fiveonestudy.ddait.study.repository;

import fiveonestudy.ddait.study.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
}