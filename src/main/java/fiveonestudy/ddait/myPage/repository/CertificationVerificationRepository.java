package fiveonestudy.ddait.myPage.repository;

import fiveonestudy.ddait.myPage.entity.CertificationVerification;
import fiveonestudy.ddait.myPage.entity.UserCertification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationVerificationRepository extends JpaRepository<CertificationVerification, Long> {
    Optional<CertificationVerification> findByUserCertificationOrderByCreatedAtDesc(UserCertification userCertification);
    void deleteByUserCertification(UserCertification userCertification);
}