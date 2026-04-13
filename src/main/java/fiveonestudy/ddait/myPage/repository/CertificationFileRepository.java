package fiveonestudy.ddait.myPage.repository;

import fiveonestudy.ddait.myPage.entity.CertificationFile;
import fiveonestudy.ddait.myPage.entity.UserCertification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationFileRepository extends JpaRepository<CertificationFile, Long> {
    CertificationFile deleteByUserCertification(UserCertification userCertification);
    Optional<CertificationFile> findByUserCertificationId(Long userCertificationId);
}