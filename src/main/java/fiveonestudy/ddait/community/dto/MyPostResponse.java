package fiveonestudy.ddait.community.dto;

import fiveonestudy.ddait.community.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPostResponse {
    private Long postId;
    private String title;
    private int likeCount;
    private int viewCount;

    public static MyPostResponse from(Post post) {
        return MyPostResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .build();
    }
}
