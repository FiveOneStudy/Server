package fiveonestudy.ddait.plan.repository;

import fiveonestudy.ddait.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByEmailAndDate(String email, LocalDate date);
    List<Plan> findByEmailAndDateBetween(String email, LocalDate startDate, LocalDate endDate);

    Optional<Plan> findByEmailAndDateAndPlanContent(String email, LocalDate date, String content);
}