package fiveonestudy.ddait.community.dto;

import fiveonestudy.ddait.community.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyCommentResponse {
    private Long commentId;
    private String content;
    private Long postId;

    public static MyCommentResponse from(Comment comment) {
        return MyCommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .build();
    }
}
