package fiveonestudy.ddait.myPage.repository;

import fiveonestudy.ddait.myPage.entity.CertificationStatus;
import fiveonestudy.ddait.myPage.entity.UserCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface UserCertificationRepository extends JpaRepository<UserCertification, Long> {
    List<UserCertification> findByUserId(Long userId);
    List<UserCertification> findByStatusOrderById(CertificationStatus status);

    @Query("""
            SELECT COUNT(uc) > 0
            FROM UserCertification uc
            WHERE uc.user.id = :userId
              AND uc.certification.name = :certificationName
              AND uc.status IN :statuses
            """)
    boolean existsActiveCertificationName(
            @Param("userId") Long userId,
            @Param("certificationName") String certificationName,
            @Param("statuses") Collection<CertificationStatus> statuses
    );

    @Query("SELECT uc FROM UserCertification uc JOIN FETCH uc.certification WHERE uc.user.email = :email")
    List<UserCertification> findByUserEmailWithCertification(@Param("email") String email);
}