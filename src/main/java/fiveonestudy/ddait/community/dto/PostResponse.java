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
    private Long userId;
    private String writer;
    private LocalDate createdAt;
    private boolean canDelete;
    private Long nextPostId;

    public static PostResponse from(Post post) {
        return from(post, null, null);
    }

    public static PostResponse from(Post post, User currentUser, Long nextPostId) {

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
                .userId(post.getUser().getId())
                .createdAt(post.getCreatedAt().toLocalDate())
                .canDelete(canDelete)
                .nextPostId(nextPostId)
                .build();
    }
}