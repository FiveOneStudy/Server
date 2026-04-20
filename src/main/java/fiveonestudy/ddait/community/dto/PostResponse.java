package fiveonestudy.ddait.community.dto;

import fiveonestudy.ddait.community.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponse {

    private Long postId;
    private String title;
    private String content;
    private int likeCount;
    private int viewCount;
    private String writer;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .writer(post.getUser().getNickname())
                .build();
    }
}