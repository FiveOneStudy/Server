package fiveonestudy.ddait.community.dto;

import fiveonestudy.ddait.community.entity.Post;
import fiveonestudy.ddait.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MyPostResponse {
    private Long postId;
    private String title;
    private int likeCount;
    private int viewCount;
    private LocalDate createdAt;
    private String writer;

    public static MyPostResponse from(Post post) {
        return MyPostResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt().toLocalDate())
                .writer(post.getUser().getNickname())
                .build();
    }
}
