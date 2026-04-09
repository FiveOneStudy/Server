package fiveonestudy.ddait.myPage.entity;

import fiveonestudy.ddait.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_certifications")
public class UserCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id", nullable = false)
    private Certification certification;

    @Column(nullable = false)
    private LocalDate acquiredDate;

    private String verifiedId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificationStatus status = CertificationStatus.PENDING;

    public void approve() {
        if (this.status != CertificationStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 자격증");
        }
        this.status = CertificationStatus.APPROVED;
    }

    public void reject() {
        if (this.status != CertificationStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 자격증");
        }
        this.status = CertificationStatus.REJECTED;
    }

    public static UserCertification create(User user, Certification certification, LocalDate acquiredDate, String verifiedId) {
        UserCertification uc = new UserCertification();
        uc.user = user;
        uc.certification = certification;
        uc.acquiredDate = acquiredDate;
        uc.verifiedId = verifiedId;
        uc.status = CertificationStatus.PENDING;
        return uc;
    }
}