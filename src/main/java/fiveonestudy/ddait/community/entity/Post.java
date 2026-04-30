package fiveonestudy.ddait.community.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import fiveonestudy.ddait.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;

    @Lob
    private String content;

    private int likeCount;

    private int viewCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void incrementViewCount() {
        this.viewCount++;
    }
    public void incrementLikeCount() {
        this.likeCount++;
    }
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Builder
    public Post(User user, String title, String content, PostStatus status) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.status = status == null ? PostStatus.PENDING : status;
        this.likeCount = 0;
        this.viewCount = 0;
    }

    public void setStatus(PostStatus postStatus) {
        this.status = postStatus;
    }
}
