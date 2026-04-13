package fiveonestudy.ddait.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String oauthId;

    @Column(unique = true)
    private String email;

    private String password;

    @Setter
    @Column(nullable = false)
    private String nickname;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    @Column(name = "profile_image_type")
    private String profileImageType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String refreshToken;

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void updateProfileImage(byte[] image, String contentType) {
        this.profileImage = image;
        this.profileImageType = contentType;
    }

    public Boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }
}