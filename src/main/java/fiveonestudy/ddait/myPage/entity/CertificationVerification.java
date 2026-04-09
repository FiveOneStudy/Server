package fiveonestudy.ddait.myPage.entity;

import fiveonestudy.ddait.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "certification_verifications")
public class CertificationVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 검증 대상
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_certification_id", nullable = false)
    private UserCertification userCertification;

    // 관리자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificationStatus status;

    private String reason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public CertificationVerification(UserCertification uc, User admin, CertificationStatus status, String reason) {
        this.userCertification = uc;
        this.admin = admin;
        this.status = status;
        this.reason = reason;
    }
}