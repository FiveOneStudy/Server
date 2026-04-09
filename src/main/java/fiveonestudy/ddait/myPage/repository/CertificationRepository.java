package fiveonestudy.ddait.myPage.repository;

import fiveonestudy.ddait.myPage.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    Optional<Certification> findByName(String name);
}