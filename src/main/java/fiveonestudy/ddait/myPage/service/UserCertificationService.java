package fiveonestudy.ddait.myPage.service;

import fiveonestudy.ddait.myPage.dto.CertificationRequest;
import fiveonestudy.ddait.myPage.entity.Certification;
import fiveonestudy.ddait.myPage.entity.CertificationFile;
import fiveonestudy.ddait.myPage.entity.UserCertification;
import fiveonestudy.ddait.myPage.repository.CertificationFileRepository;
import fiveonestudy.ddait.myPage.repository.CertificationRepository;
import fiveonestudy.ddait.myPage.repository.UserCertificationRepository;
import fiveonestudy.ddait.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class UserCertificationService {

    private final UserCertificationRepository userCertificationRepository;
    private final CertificationRepository certificationRepository;
    private final CertificationFileRepository certificationFileRepository;

    public Long register(User user, CertificationRequest request, MultipartFile file) throws Exception {

        if (user == null) {
            throw new RuntimeException("UNAUTHORIZED");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("INVALID_REQUEST");
        }

        if (request.getIssuer() == null || request.getIssuer().isBlank()) {
            throw new IllegalArgumentException("INVALID_REQUEST");
        }

        if (request.getAcquiredDate() == null) {
            throw new IllegalArgumentException("INVALID_REQUEST");
        }

        if (file.isEmpty()) {
            throw new RuntimeException("파일 필수");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("파일 크기 초과 (5MB)");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("application/pdf") &&
                        !contentType.startsWith("image/"))) {
            throw new RuntimeException("INVALID_FILE_TYPE");
        }

        Certification certification = certificationRepository.findByName(request.getName())
                .orElseGet(() -> certificationRepository.save(
                        new Certification(request.getName(), request.getIssuer(), null)
                ));

        UserCertification uc = UserCertification.create(
                user,
                certification,
                request.getAcquiredDate(),
                request.getVerifiedId()
        );

        userCertificationRepository.save(uc);

        CertificationFile certFile = new CertificationFile(
                uc,
                file.getBytes(),
                file.getOriginalFilename(),
                contentType
        );

        certificationFileRepository.save(certFile);

        return uc.getId();
    }

    @Transactional(readOnly = true)
    public List<UserCertification> getMyCertifications(Long userId) {
        return userCertificationRepository.findByUserId(userId);
    }
}