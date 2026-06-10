package fiveonestudy.ddait.community.dto;

import fiveonestudy.ddait.community.entity.Post;
import fiveonestudy.ddait.user.entity.Role;
import fiveonestudy.ddait.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PostResponse {

    private Long postId;
    private String title;
    private String content;
    private int likeCount;
    private int viewCount;
    private String writerEmail;
    private String writer;
    private LocalDate createdAt;
    private boolean canDelete;

    public static PostResponse from(Post post) {
        return from(post, null);
    }

    public static PostResponse from(Post post, User currentUser) {
        boolean canDelete = currentUser != null
                && (post.getUser().getId().equals(currentUser.getId())
                || currentUser.getRole() == Role.ADMIN);

        return PostResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .writer(post.getUser().getNickname())
                .writerEmail(post.getUser().getEmail())
                .createdAt(post.getCreatedAt().toLocalDate())
                .canDelete(canDelete)
                .build();
    }
}