package fiveonestudy.ddait.admin.service;

import fiveonestudy.ddait.myPage.entity.CertificationStatus;
import fiveonestudy.ddait.myPage.entity.CertificationVerification;
import fiveonestudy.ddait.myPage.entity.UserCertification;
import fiveonestudy.ddait.myPage.repository.CertificationVerificationRepository;
import fiveonestudy.ddait.myPage.repository.UserCertificationRepository;
import fiveonestudy.ddait.security.CustomUserDetails;
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
        return userCertificationRepository.findByStatusOrderById(CertificationStatus.PENDING);
    }

    public void approve(Long userCertificationId, CustomUserDetails adminDetails) {

        if (adminDetails == null || !adminDetails.getUser().isAdmin()) {
            throw new RuntimeException("관리자 권한 필요");
        }

        UserCertification uc = userCertificationRepository.findById(userCertificationId)
                .orElseThrow(() -> new RuntimeException("자격증 없음"));

        uc.approve();

        verificationRepository.save(
                new CertificationVerification(uc, adminDetails.getUser(), CertificationStatus.APPROVED, null)
        );
    }

    public void reject(Long userCertificationId, CustomUserDetails adminDetails, String reason) {

        if (adminDetails == null || !adminDetails.getUser().isAdmin()) {
            throw new RuntimeException("관리자 권한 필요");
        }

        UserCertification uc = userCertificationRepository.findById(userCertificationId)
                .orElseThrow(() -> new RuntimeException("자격증 없음"));

        uc.reject();

        verificationRepository.save(
                new CertificationVerification(uc, adminDetails.getUser(), CertificationStatus.REJECTED, reason)
        );
    }
}