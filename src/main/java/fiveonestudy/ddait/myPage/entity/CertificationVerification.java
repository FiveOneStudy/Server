package fiveonestudy.ddait.myPage.entity;

import fiveonestudy.ddait.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
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
}