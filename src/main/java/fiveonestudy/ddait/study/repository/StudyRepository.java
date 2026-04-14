package fiveonestudy.ddait.study.repository;

import fiveonestudy.ddait.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {
    List<Study> findByNameContaining(String name);
}