package fiveonestudy.ddait.myPage.repository;

import fiveonestudy.ddait.myPage.entity.CertificationFile;
import fiveonestudy.ddait.myPage.entity.CertificationStatus;
import fiveonestudy.ddait.myPage.entity.UserCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface CertificationFileRepository extends JpaRepository<CertificationFile, Long> {
    void deleteByUserCertification(UserCertification userCertification);
    Optional<CertificationFile> findByUserCertificationId(Long userCertificationId);

    @Query("""
            SELECT COUNT(cf) > 0
            FROM CertificationFile cf
            WHERE cf.userCertification.user.id = :userId
              AND cf.originalName = :originalName
              AND cf.userCertification.status IN :statuses
            """)
    boolean existsActiveOriginalName(
            @Param("userId") Long userId,
            @Param("originalName") String originalName,
            @Param("statuses") Collection<CertificationStatus> statuses
    );
}