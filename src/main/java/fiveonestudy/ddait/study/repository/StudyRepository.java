package fiveonestudy.ddait.study.repository;

import fiveonestudy.ddait.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}