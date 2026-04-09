package fiveonestudy.ddait.myPage.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "certifications", uniqueConstraints = @UniqueConstraint(name = "uk_cert_name", columnNames = "name"))
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String issuer;

    private Integer prize;

}
