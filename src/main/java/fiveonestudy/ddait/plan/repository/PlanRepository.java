package fiveonestudy.ddait.plan.repository;

import fiveonestudy.ddait.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByEmailAndDate(String email, LocalDate date);

    List<Plan> findByEmailAndDateBetween(String email, LocalDate startDate, LocalDate endDate);
}