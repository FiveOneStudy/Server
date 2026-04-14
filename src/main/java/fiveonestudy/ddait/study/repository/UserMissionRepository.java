package fiveonestudy.ddait.study.repository;

import fiveonestudy.ddait.study.entity.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
}