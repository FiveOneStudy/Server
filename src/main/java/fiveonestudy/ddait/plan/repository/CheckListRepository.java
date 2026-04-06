package fiveonestudy.ddait.plan.repository;

import fiveonestudy.ddait.plan.entity.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CheckListRepository extends JpaRepository<CheckList, Long> {

    List<CheckList> findByEmailAndDate(String email, LocalDate date);
    Optional<CheckList> findByEmailAndDateAndCheckContent(String email, LocalDate date, String content);
}