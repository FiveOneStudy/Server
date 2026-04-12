package fiveonestudy.ddait.myPage.repository;

import fiveonestudy.ddait.myPage.entity.CertificationFile;
import fiveonestudy.ddait.myPage.entity.UserCertification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationFileRepository extends JpaRepository<CertificationFile, Long> {
    CertificationFile deleteByUserCertification(UserCertification userCertification);
}