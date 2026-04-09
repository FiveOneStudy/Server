package fiveonestudy.ddait.myPage.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "certification_files")
public class CertificationFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_certification_id", nullable = false)
    private UserCertification userCertification;

    @Column(nullable = false)
    private String fileUrl;

    private String originalName;

    private String contentType;

    @CreationTimestamp
    private LocalDateTime createdAt;
}