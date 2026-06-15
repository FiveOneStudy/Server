package fiveonestudy.ddait.community.dto;

import fiveonestudy.ddait.community.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MyCommentResponse {
    private Long commentId;
    private String content;
    private Long postId;
    private Long userId;
    private LocalDate createdAt;
    private String writer;

    public static MyCommentResponse from(Comment comment) {
        return MyCommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .createdAt(comment.getCreatedAt().toLocalDate())
                .writer(comment.getUser().getNickname())
                .build();
    }
}
