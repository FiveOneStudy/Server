package fiveonestudy.ddait.community.dto;

import fiveonestudy.ddait.community.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {

    private Long commentId;
    private String content;
    private String writer;
    private Long parentId;
    private boolean deleted;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.isDeleted() ? "삭제된 댓글입니다." : comment.getContent())
                .writer(comment.getUser().getNickname())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .deleted(comment.isDeleted())
                .build();
    }
}