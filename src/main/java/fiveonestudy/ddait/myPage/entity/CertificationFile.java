package fiveonestudy.ddait.myPage.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "certification_files")
public class CertificationFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_certification_id", nullable = false)
    private UserCertification userCertification;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] fileData;

    private String originalName;

    @Column(nullable = false)
    private String contentType;


    public CertificationFile(UserCertification userCertification, byte[] fileData, String originalName, String contentType) {
        this.userCertification = userCertification;
        this.fileData = fileData;
        this.originalName = originalName;
        this.contentType = contentType;
    }
}