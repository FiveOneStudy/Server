package fiveonestudy.ddait.myPage.repository;

import fiveonestudy.ddait.myPage.entity.CertificationVerification;
import fiveonestudy.ddait.myPage.entity.UserCertification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationVerificationRepository extends JpaRepository<CertificationVerification, Long> {
    void deleteByUserCertification(UserCertification userCertification);
}