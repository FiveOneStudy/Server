package fiveonestudy.ddait.myPage.service;

import fiveonestudy.ddait.myPage.entity.CertificationStatus;
import fiveonestudy.ddait.myPage.entity.CertificationVerification;
import fiveonestudy.ddait.myPage.entity.UserCertification;
import fiveonestudy.ddait.myPage.repository.CertificationVerificationRepository;
import fiveonestudy.ddait.myPage.repository.UserCertificationRepository;
import fiveonestudy.ddait.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCertificationService {

    private final UserCertificationRepository userCertificationRepository;
    private final CertificationVerificationRepository verificationRepository;

    @Transactional(readOnly = true)
    public List<UserCertification> getPendingList() {
        return userCertificationRepository.findByStatus(CertificationStatus.PENDING);
    }

    public void approve(Long userCertificationId, User admin) {

        if (admin == null || !admin.isAdmin()) {
            throw new RuntimeException("관리자 권한 필요");
        }

        UserCertification uc = userCertificationRepository.findById(userCertificationId)
                .orElseThrow(() -> new RuntimeException("자격증 없음"));

        uc.approve();

        verificationRepository.save(
                new CertificationVerification(uc, admin, CertificationStatus.APPROVED, null)
        );
    }

    public void reject(Long userCertificationId, User admin, String reason) {

        if (admin == null || !admin.isAdmin()) {
            throw new RuntimeException("관리자 권한 필요");
        }

        UserCertification uc = userCertificationRepository.findById(userCertificationId)
                .orElseThrow(() -> new RuntimeException("자격증 없음"));

        uc.reject();

        verificationRepository.save(
                new CertificationVerification(uc, admin, CertificationStatus.REJECTED, reason)
        );
    }
}