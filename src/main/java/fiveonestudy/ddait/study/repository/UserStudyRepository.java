package fiveonestudy.ddait.study.repository;

import fiveonestudy.ddait.study.entity.UserStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStudyRepository extends JpaRepository<UserStudy, Long> {
    List<UserStudy> findByUserName(String userName);
}